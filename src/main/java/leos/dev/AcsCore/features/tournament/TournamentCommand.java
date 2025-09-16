package leos.dev.AcsCore.features.tournament;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TournamentCommand implements TabExecutor, Listener {
    private final AcsCore plugin;
    private final Map<String, Tournament> tournaments = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerToTournament = new ConcurrentHashMap<>();
    private final Map<UUID, Instant> offlineTimes = new ConcurrentHashMap<>();

    public TournamentCommand(AcsCore plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    static class Tournament {
        public final String id;
        public final List<UUID> team1;
        public final List<UUID> team2;
        public final Set<UUID> dead = new HashSet<>();
        public final Instant startTime = Instant.now();
        public boolean started = false;
        public boolean finished = false;
        public UUID host;
        public final Scoreboard scoreboard;
        public final Objective objective;
        public BukkitRunnable scoreboardTask;

        public Tournament(String id, List<UUID> t1, List<UUID> t2, UUID host, Scoreboard sb, Objective obj) {
            this.id=id; this.team1=t1; this.team2=t2; this.host=host; this.scoreboard=sb; this.objective=obj;
        }

        public List<UUID> allPlayers() {
            List<UUID> all = new ArrayList<>(team1); all.addAll(team2); return all;
        }

        public boolean teamDead(List<UUID> t) { return t.stream().allMatch(dead::contains); }
    }

    private boolean canManage(CommandSender sender, Tournament t) {
        if (sender.hasPermission("acscore.tournament.manage") || sender.isOp()) return true;
        if (sender instanceof Player p && t != null && t.host != null && t.host.equals(p.getUniqueId())) return true;
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (args.length == 0) {
            usage(sender);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "start" -> start(sender, args);
            case "stop" -> stop(sender, args);
            case "disqualify" -> disqualify(sender, args);
            case "revive" -> revive(sender, args);
            case "host" -> host(sender, args);
            case "info" -> info(sender, args);
            default -> usage(sender);
        }
        return true;
    }

    private void usage(CommandSender s) {
        s.sendMessage("§f/tournament start <team1...> vs <team2...>");
        s.sendMessage("§f/tournament stop <player|id>");
        s.sendMessage("§f/tournament disqualify <p1...>");
        s.sendMessage("§f/tournament revive <p1...>");
        s.sendMessage("§f/tournament host <player>");
        s.sendMessage("§f/tournament info [player]");
    }

    private void start(CommandSender sender, String[] args) {
        if (args.length < 4) { sender.sendMessage("§cUsage: /tournament start <team1...> vs <team2...>"); return; }
        int vs = -1;
        for (int i=1;i<args.length;i++) if (args[i].equalsIgnoreCase("vs")) { vs = i; break; }
        if (vs <=1 || vs == args.length-1) { sender.sendMessage("§cUsage: /tournament start <team1...> vs <team2...>"); return; }
        Set<Player> all = new HashSet<>();
        List<Player> t1 = new ArrayList<>();
        List<Player> t2 = new ArrayList<>();
        for (int i=1;i<vs;i++) {
            Player p = Bukkit.getPlayerExact(args[i]);
            if (p==null){ sender.sendMessage("§cMissing "+args[i]); return; }
            if (!all.add(p)) { sender.sendMessage("§cDuplicate "+p.getName()); return; }
            t1.add(p);
        }
        for (int i=vs+1;i<args.length;i++) {
            Player p = Bukkit.getPlayerExact(args[i]);
            if (p==null){ sender.sendMessage("§cMissing "+args[i]); return; }
            if (!all.add(p)) { sender.sendMessage("§cDuplicate "+p.getName()); return; }
            t2.add(p);
        }
        if (t1.isEmpty() || t2.isEmpty()) { sender.sendMessage("§cTeams cannot be empty"); return; }
        for (Player p : all) if (playerToTournament.containsKey(p.getUniqueId())) { sender.sendMessage("§c"+p.getName()+" already in tournament"); return; }
        String id = UUID.randomUUID().toString().substring(0,8);
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = sb.registerNewObjective("tournament","dummy","§x§D§E§B§1§F§BAcsTournament");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        UUID host = sender instanceof Player pl ? pl.getUniqueId() : null;
        Tournament t = new Tournament(id, t1.stream().map(Player::getUniqueId).toList(), t2.stream().map(Player::getUniqueId).toList(), host, sb, obj);
        tournaments.put(id, t);
        for (Player p : all) playerToTournament.put(p.getUniqueId(), id);
        countdown(t, t1, t2, sender);
        sender.sendMessage("§aTournament "+id+" starting...");
    }

    private void countdown(Tournament t, List<Player> team1, List<Player> team2, CommandSender host) {
        List<Player> all = new ArrayList<>(team1); all.addAll(team2);
        Player hostPlayer = host instanceof Player hp ? hp : null;
        new BukkitRunnable() {
            int c=5;
            @Override
            public void run() {
                List<Player> rec = new ArrayList<>(all);
                if (hostPlayer != null && !rec.contains(hostPlayer)) rec.add(hostPlayer);
                if (c>0) {
                    for (Player pl : rec) {
                        pl.sendTitle("§x§D§E§B§1§F§B"+c,"",5,15,5);
                        pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT,1f,1f);
                    }
                    c--;
                } else {
                    for (Player pl : rec) {
                        pl.sendTitle("§aStart!","",10,40,10);
                        pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,1f,1f);
                        pl.setScoreboard(t.scoreboard);
                    }
                    t.started = true;
                    startBoardTask(t);
                    cancel();
                }
            }
        }.runTaskTimer(plugin,0L,20L);
    }

    private void startBoardTask(Tournament t) {
        t.scoreboardTask = new BukkitRunnable() {
            @Override public void run() {
                if (t.finished) { cancel(); return; }
                updateBoard(t);
            }
        };
        t.scoreboardTask.runTaskTimer(plugin,0L,20L);
    }

    private void updateBoard(Tournament t) {
        for (String e : new ArrayList<>(t.scoreboard.getEntries())) t.scoreboard.resetScores(e);
        Objective o = t.objective;
        int score = 200;
        o.getScore("§f").setScore(score--);
        o.getScore("§9§lTeam 1").setScore(score--);
        for (UUID u : t.team1) {
            String n = Optional.ofNullable(Bukkit.getOfflinePlayer(u).getName()).orElse("?");
            o.getScore(t.dead.contains(u)?"§7§m"+n:"§9"+n).setScore(score--);
        }
        o.getScore(" ").setScore(score--);
        o.getScore("§c§lTeam 2").setScore(score--);
        for (UUID u : t.team2) {
            String n = Optional.ofNullable(Bukkit.getOfflinePlayer(u).getName()).orElse("?");
            o.getScore(t.dead.contains(u)?"§7§m"+n:"§c"+n).setScore(score--);
        }
        long dur = Duration.between(t.startTime, Instant.now()).getSeconds();
        o.getScore("§x§D§E§B§1§F§BTime: "+String.format("%02d:%02d", dur/60, dur%60)).setScore(score--);
        if (!t.finished) {
            if (t.teamDead(t.team1)) finish(t, t.team2);
            else if (t.teamDead(t.team2)) finish(t, t.team1);
        }
    }

    private void finish(Tournament t, List<UUID> winner) {
        t.finished = true;
        if (t.scoreboardTask != null) t.scoreboardTask.cancel();
        for (UUID u : winner) {
            Player p = Bukkit.getPlayer(u);
            if (p != null) p.sendTitle("§aVictory!","",10,60,10);
        }
        new BukkitRunnable() {
            @Override public void run() { end(t,true); }
        }.runTaskLater(plugin,200L);
    }

    private void stop(CommandSender sender, String[] args) {
        if (args.length<2) { sender.sendMessage("§cUsage: /tournament stop <player|id>"); return; }
        Tournament t = byPlayerOrId(args[1]);
        if (t==null) { sender.sendMessage("§cNot found"); return; }
        if (!canManage(sender,t)) { sender.sendMessage("§cNo permission"); return; }
        end(t,false);
        sender.sendMessage("§7Stopped.");
    }

    private void disqualify(CommandSender sender, String[] args) {
        if (args.length<2) { sender.sendMessage("§cUsage: /tournament disqualify <p1...>"); return; }
        Tournament t = null;
        int count=0;
        for (int i=1;i<args.length;i++) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(args[i]);
            if (t==null) t = byPlayer(op.getUniqueId());
            if (t==null) { sender.sendMessage("§cPlayer "+args[i]+" not in tournament"); return; }
            if (!canManage(sender,t)) { sender.sendMessage("§cNo permission"); return; }
            if (t.dead.add(op.getUniqueId())) count++;
        }
        if (t!=null) {
            updateBoard(t);
            sender.sendMessage("§aDisqualified "+count);
        }
    }

    private void revive(CommandSender sender, String[] args) {
        if (args.length<2) { sender.sendMessage("§cUsage: /tournament revive <p1...>"); return; }
        Tournament t=null;
        int count=0;
        for (int i=1;i<args.length;i++) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(args[i]);
            if (t==null) t = byPlayer(op.getUniqueId());
            if (t==null) { sender.sendMessage("§cPlayer "+args[i]+" not in tournament"); return; }
            if (!canManage(sender,t)) { sender.sendMessage("§cNo permission"); return; }
            if (t.dead.remove(op.getUniqueId())) count++;
        }
        if (t!=null) {
            updateBoard(t);
            sender.sendMessage("§aRevived "+count);
        }
    }

    private void host(CommandSender sender, String[] args) {
        if (!(sender instanceof Player pl)) { sender.sendMessage("§cOnly players."); return; }
        if (args.length!=2) { sender.sendMessage("§cUsage: /tournament host <player>"); return; }
        Tournament t = byPlayer(pl.getUniqueId());
        if (t==null) { sender.sendMessage("§cNot in tournament"); return; }
        if (!canManage(sender,t)) { sender.sendMessage("§cNo permission"); return; }
        Player newHost = Bukkit.getPlayerExact(args[1]);
        if (newHost==null) { sender.sendMessage("§cPlayer not found"); return; }
        t.host = newHost.getUniqueId();
        newHost.sendMessage("§aYou are now host of tournament "+t.id);
        sender.sendMessage("§aHost transferred.");
    }

    private void info(CommandSender sender, String[] args) {
        Tournament t=null;
        if (args.length==2) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
            t = byPlayer(op.getUniqueId());
        } else if (sender instanceof Player p) {
            t = byPlayer(p.getUniqueId());
        }
        if (t==null) { sender.sendMessage("§cNo tournament"); return; }
        sender.sendMessage("§7Tournament §f"+t.id+(t.finished?" §c(FINISHED)":""));
        sender.sendMessage("§7Host: §f"+(t.host==null?"none":Optional.ofNullable(Bukkit.getOfflinePlayer(t.host).getName()).orElse("Unknown")));
        sender.sendMessage("§9Team1: §f"+listTeam(t.team1, t.dead));
        sender.sendMessage("§cTeam2: §f"+listTeam(t.team2, t.dead));
        long secs = Duration.between(t.startTime, Instant.now()).getSeconds();
        sender.sendMessage("§7Time: §f"+String.format("%02d:%02d", secs/60, secs%60));
    }

    private String listTeam(List<UUID> team, Set<UUID> dead) {
        return team.stream().map(u -> {
            String n = Optional.ofNullable(Bukkit.getOfflinePlayer(u).getName()).orElse("?");
            return dead.contains(u)?"§7§m"+n+"§r":n;
        }).collect(Collectors.joining(", "));
    }

    private Tournament byPlayer(UUID u) {
        String id = playerToTournament.get(u);
        return id==null?null:tournaments.get(id);
    }

    private Tournament byPlayerOrId(String token) {
        for (Tournament t : tournaments.values()) if (t.id.equalsIgnoreCase(token)) return t;
        Player p = Bukkit.getPlayerExact(token);
        if (p!=null) return byPlayer(p.getUniqueId());
        OfflinePlayer op = Bukkit.getOfflinePlayer(token);
        if (op!=null) return byPlayer(op.getUniqueId());
        return null;
    }

    private void end(Tournament t, boolean silent) {
        tournaments.remove(t.id);
        if (t.scoreboardTask != null) t.scoreboardTask.cancel();
        for (UUID u : t.allPlayers()) {
            playerToTournament.remove(u);
            Player p = Bukkit.getPlayer(u);
            if (p!=null) {
                if (!silent) p.sendMessage("§cTournament ended.");
                p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
        }
    }

    @EventHandler public void onDeath(PlayerDeathEvent e) {
        Tournament t = byPlayer(e.getEntity().getUniqueId());
        if (t==null || !t.started || t.finished) return;
        t.dead.add(e.getEntity().getUniqueId());
    }
    @EventHandler public void onQuit(PlayerQuitEvent e) {
        Tournament t = byPlayer(e.getPlayer().getUniqueId());
        if (t==null || !t.started || t.finished) return;
        offlineTimes.put(e.getPlayer().getUniqueId(), Instant.now());
        new BukkitRunnable() {
            @Override public void run() {
                if (Bukkit.getPlayer(e.getPlayer().getUniqueId()) == null) t.dead.add(e.getPlayer().getUniqueId());
                else offlineTimes.remove(e.getPlayer().getUniqueId());
            }
        }.runTaskLater(plugin, 20L*30);
    }
    @EventHandler public void onJoin(PlayerJoinEvent e) {
        Tournament t = byPlayer(e.getPlayer().getUniqueId());
        if (t!=null && t.started && !t.finished) e.getPlayer().setScoreboard(t.scoreboard);
        offlineTimes.remove(e.getPlayer().getUniqueId());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> online = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        List<String> out = new ArrayList<>();
        if (args.length==1) {
            for (String s : List.of("start","stop","disqualify","revive","host","info")) if (s.startsWith(args[0].toLowerCase())) out.add(s);
            return out;
        }
        if (args[0].equalsIgnoreCase("start")) {
            String last = args[args.length-1].toLowerCase();
            boolean hasVs = Arrays.stream(args).anyMatch(a->a.equalsIgnoreCase("vs"));
            if (!hasVs && "vs".startsWith(last)) out.add("vs");
            for (String n : online) {
                boolean used=false;
                for (int i=1;i<args.length;i++) if (args[i].equalsIgnoreCase(n)) { used=true; break; }
                if (!used && n.toLowerCase().startsWith(last)) out.add(n);
            }
            return out;
        }
        if (List.of("disqualify","revive","host","stop","info").contains(args[0].toLowerCase())) {
            String last = args[args.length-1].toLowerCase();
            for (String n : online) if (n.toLowerCase().startsWith(last)) out.add(n);
        }
        return out;
    }
}