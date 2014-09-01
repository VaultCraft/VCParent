package net.vaultcraft.vcessentials.commands;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.chat.bot.ChatterBot;
import net.vaultcraft.vcutils.chat.bot.ChatterBotFactory;
import net.vaultcraft.vcutils.chat.bot.ChatterBotSession;
import net.vaultcraft.vcutils.chat.bot.ChatterBotType;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 8/27/14. Designed for the VCUtils project.
 */

public class VCSpamFunStuff extends ICommand {

    public VCSpamFunStuff(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    private static VCBotInstance bot = null;

    public void processCommand(Player player, String[] args) {
        if (bot != null) {
            bot.alive = false;
            bot = null;

            Form.at(player, Prefix.WARNING, "Current bot killed!");
            return;
        }

        bot = new VCBotInstance(mcNames[(int)(Math.random()*mcNames.length)], mcNames[(int)(Math.random()*mcNames.length)]);

        Form.at(player, Prefix.SUCCESS, "Chat bots created! Have fun trolling nubs");
    }

    private static class VCBotInstance {

        private boolean alive = true;

        private static ChatterBotFactory fac;

        private String u1;
        private String u2;

        private ChatterBotSession u1Sess;
        private ChatterBotSession u2Sess;

        private ChatterBot u1Bot;
        private ChatterBot u2Bot;

        String old;
        private boolean wB = false;

        public VCBotInstance(String u1, String u2) {
            this.u1 = u1;
            this.u2 = u2;

            this.old = "Hi I'm "+u1;

            try {
                if (fac == null)
                    fac = new ChatterBotFactory();

                u1Bot = fac.create(ChatterBotType.JABBERWACKY);
                u2Bot = fac.create(ChatterBotType.JABBERWACKY);

                u1Sess = u1Bot.createSession();
                u2Sess = u2Bot.createSession();

                tick();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void tick() {
            Runnable run = new Runnable() {
                public void run() {
                    wB = !wB;

                    if (wB) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7"+u1+"&f: &7"+old));

                        try {
                            old = u2Sess.think(old);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7"+u2+"&f: &7"+old));

                        try {
                            old = u1Sess.think(old);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (!(alive))
                        return;

                    tick();
                }
            };
            Bukkit.getScheduler().scheduleAsyncDelayedTask(VCEssentials.getInstance(), run,
                    (int) (20 * ((Math.random() * 5 + 3))));
        }
    }

    private static String[] mcNames = {
            "00285641",
            "0SV4LD0",
            "1000cupcakes",
            "11Echo",
            "123marine2",
            "1panda80",
            "21texans",
            "3DS_Triforce",
            "3rdbaseman4",
            "459pm",
            "4everfuzzy",
            "4lphax",
            "4sordy",
            "77chainblade",
            "99milzman",
            "Aaron_Rainthief",
            "ABQiu",
            "absthatsme",
            "abysmus",
            "abzilla",
            "acidsin",
            "acraftybugger",
            "adambuss",
            "adampoconnor",
            "adamzetti",
            "adolia",
            "Adriedupleaf",
            "Aerinyes",
            "aetherandnether",
            "AFRICA",
            "aidanrocks25",
            "ailbhlaffe",
            "Ainieve",
            "Akathepriest",
            "Alec97531",
            "alecf731",
            "alexryanb",
            "alexthecoolmac",
            "Alizatina",
            "Allyvand1497",
            "alphaelf",
            "Amortik1996",
            "Anactofgod",
            "andrewf731",
            "Andy586",
            "AndyWestside",
            "angrysquirlz",
            "Anhysbys",
            "Anthony423",
            "antz666",
            "aperry1993",
            "ApocalyqseNow",
            "Aqua2iK",
            "aqwshalew3",
            "ArinaChan23",
            "Armydude101",
            "Artemis315",
            "Artemis_En",
            "articuno96",
            "asbo96",
            "Ashmen",
            "asmith307",
            "aTbagger",
            "atlanta0",
            "atyl95",
            "AustinSpiers",
            "Avengeline",
            "Awdie",
            "AwesomeDude728",
            "AwesomeGuy900",
            "awesomeguy911",
            "Awsomr00",
            "ayrtonchin",
            "Azasimus",
            "azureheights",
            "B1GBADW0IF",
            "b777all",
            "babyface0519",
            "bailout00",
            "baller111",
            "Banaynay",
            "bandrew97",
            "Barbaric_Emu",
            "basstardee",
            "Batyote",
            "Bball_Star",
            "bbrooks066",
            "Belaneth",
            "Ben952",
            "Benben582",
            "Benn_Benn",
            "benpowell987",
            "bevo00",
            "bgeaman",
            "Bigalow40",
            "BigGojira",
            "BigKid_Icarus",
            "BigLlama",
            "BigMucho",
            "Bigtimewinner",
            "BIG_Quakez",
            "birdman15",
            "birdy9",
            "bizznchriz",
            "Bkettell",
            "bkwopper",
            "blackdust79",
            "blackfire9z",
            "blackhawk77g",
            "blackouTT",
            "blackstorm58",
            "blake11120",
            "BlastedFool",
            "blazindonuts824",
            "BleakBear",
            "bleh337",
            "Blimm",
            "Blitzfitz",
            "BlockedShots",
            "Bloodlest",
            "Bloodnight",
            "Bloodtyl",
            "blurrdew",
            "bluzekluze",
            "bmanpyro",
            "BMXBikes58",
            "bob20163",
            "bob6199",
            "bobbshields",
            "Boborito71",
            "booglee322",
            "bookboy123",
            "bootgamer5",
            "boriater",
            "Boss_Pro",
            "bowledd",
            "boxofspiderss",
            "BoxPuncher",
            "brampel",
            "brancher",
            "brandt2846",
            "Braxis",
            "BrianOwennn",
            "BRISKET",
            "broadsword123",
            "brobot222",
            "BroncoBuster25",
            "BrookesGirl",
            "brycecameron",
            "BrylieGirl",
            "bsams5",
            "BTechnique",
            "BTT8",
            "bubabang12",
            "budro1111",
            "BugsyH",
            "buildingharry",
            "BullSabu",
            "bumscracher",
            "Bumz",
            "Burblessnot",
            "Burkester",
            "busynarcissist",
            "butnuf34",
            "Buwario",
            "bwanoodles009",
            "C00LMAN123",
            "C375",
            "Caffeine__Addict",
            "calin99",
            "CaptainMC",
            "captinbaer1",
            "caseyk1105",
            "catus123",
            "cboychuk",
            "cdog123456789",
            "cdonovan",
            "cdown13",
            "Celeress",
            "cfurn5",
            "ch0c0lat3man",
            "chall63461",
            "Chatelaine",
            "chazzam14",
            "cheatster989",
            "cheesyboy2011",
            "chengkaijie9999",
            "cherry1029384756",
            "Chewiebonez",
            "Chicken1219",
            "chickenpress75",
            "ChiCken_Ledel",
            "Chicotheman94",
            "Chieftainy",
            "Childflayer",
            "chingchong401",
            "chocolatier1991",
            "choy17",
            "Chris7692",
            "Chrisamerica",
            "chrisk11",
            "Chrustee96",
            "ChubberMummaBoy",
            "chumsly",
            "Chunnnky",
            "Cidith",
            "clampet",
            "clayqtpie",
            "CloakandDagger",
            "clocke13",
            "codepsilon",
            "codyneace",
            "Coecoepuff2",
            "cometking123",
            "CommanderCooley",
            "conman167",
            "connerk7",
            "connorr1",
            "coolguy399",
            "Cooner003",
            "cowsgopoo",
            "craftermagicman",
            "CraftinMaster117",
            "Craftking11",
            "crazy02chris",
            "CrazyBlueBannana",
            "Crazybrother",
            "crazyjake123",
            "crazziecanuck",
            "Creeper_asylum",
            "Crianade",
            "CrimsonYoshi",
            "cronocio",
            "CrowCard",
            "crystalballmac",
            "CTTheSchism",
            "CybrKrystlz",
            "c_kitai306",
            "daanisjaap",
            "DaFlash32",
            "Daggtex",
            "Dahbakon",
            "daimond84",
            "dakmech9",
            "Dalewayward",
            "damage78930",
            "DamonKBrown",
            "dan2510",
            "Dangime",
            "daniel77677",
            "danielgergely",
            "dannybtran",
            "DaOldMan",
            "darianngo",
            "DarioRVa",
            "darker1126",
            "darkmind9999",
            "darkradience",
            "DarkSoundwave",
            "Darkstorm969",
            "DarkValkare",
            "Darkwolf524",
            "Darstidos",
            "Dave5483",
            "davidnator123",
            "Dayne25",
            "DA_BLOOP",
            "DBreformed479",
            "DCXK",
            "DeadDew",
            "deadlydent101",
            "deadlytiger1",
            "DeadManJ0e13",
            "DeathAdder95",
            "Deathbreak911",
            "deathgodichigo",
            "deathmetalcop",
            "DeathNWar",
            "DecoLamb",
            "DecoyDebbie",
            "deimian254",
            "Delta_22",
            "deniz_696",
            "Denken_Polizei",
            "Deruip",
            "desmin88",
            "DesmodusDantre",
            "DeSzTr0y3r",
            "Detharein",
            "Deungo",
            "dewhickey",
            "deyjvcfg",
            "diamond_tycoon",
            "dibo",
            "Dieheart",
            "dilliwig",
            "Dillon1975",
            "Dimbulb",
            "Divine_Nightmare",
            "Dizzysquirrl",
            "DJ08",
            "DJParker",
            "djpremire",
            "dkdude2",
            "domino255",
            "DonaldDuckMG42",
            "donersayshi",
            "doombugy",
            "doorman143",
            "doorman144",
            "dragomaster69",
            "DragonHeart00",
            "DrDemocracy",
            "Dreadcor",
            "Dregamus",
            "DrRaptor",
            "Dr_Lyle",
            "duddlypuppy",
            "Dudesyaswhat1011",
            "DukeTop",
            "duramon",
            "dustangel4109",
            "Dwalsh896",
            "Dy1_",
            "dynamite4477",
            "earnhart2000",
            "Earthquick1998",
            "EaTFresH1o1",
            "elderzeth",
            "elmo2cake",
            "Elmotookmybaby",
            "elricofmelnibone",
            "el_gordo_uno",
            "emanuelllamas",
            "Eminence_k",
            "Ender6600",
            "energester",
            "EnLight707",
            "enozservant",
            "Epicelia",
            "Erazz94",
            "erikus6",
            "ert396",
            "Eruza",
            "espike7",
            "Ethdir",
            "Ever",
            "evilevan82",
            "Evilope",
            "Exacto23",
            "explosivebma",
            "Extreats",
            "FacelessPsych0",
            "failedreality",
            "FakeTruth",
            "fatiandog1234",
            "FazyTazyTraLaLa",
            "FeatheredSun",
            "Feinyth",
            "festivemaster",
            "FierceShadow",
            "Firebody",
            "firespirit44",
            "Fistbumps",
            "flashfire6970",
            "FlogtheInfidel",
            "flyers77",
            "foreverawalrus",
            "ForkliftsFTW",
            "fourthbrook",
            "foxhull",
            "foxxy2112",
            "FpsSkiller",
            "FraserK",
            "FRE3DOM_FIGHT3R",
            "FreckleDew",
            "Frenchy1",
            "friday593",
            "frodew",
            "frogface99",
            "FSUISBACK2011",
            "Fuego_Tortuga",
            "fullmetal2000",
            "gaara1125",
            "GabeSyFy",
            "gaddo_man",
            "GaidenFocus",
            "gaiusmarius8",
            "gamedude113",
            "gamer4526",
            "gamerphate",
            "Gamma_Gamer54",
            "Garet867",
            "garrettmc18",
            "Gastronam",
            "gavinmax88",
            "gemondkid",
            "GeneralDamras",
            "GeneralKash",
            "george0106",
            "georgege1229",
            "GerkJerk",
            "ggt3416",
            "ggteixeira",
            "Gibbusflame",
            "Gigglegasm",
            "Giizmo",
            "GirlyDuty",
            "Godfather5866",
            "Godock2006",
            "GodOfSquirrels",
            "Goldblade02",
            "goldenknight88",
            "Goldrim",
            "Gozar",
            "GreatBandit",
            "Greek2me",
            "greenienator",
            "GreenWins",
            "greenwolf25",
            "Gregzilla83",
            "Groobs03",
            "GrrGrrzap",
            "Gu1t4r_M4n",
            "guitar8293",
            "guitarisepic",
            "Gumat",
            "gunsbeforeroses",
            "Guswut",
            "gw4",
            "gwilson3",
            "H3artsD3ath",
            "hacchan30",
            "haggispantsrules",
            "Hallway_Monitor",
            "halodude24",
            "happypaintard",
            "HarrisonBitzis",
            "Hathegkla",
            "hawaii727",
            "Hazardless",
            "HeartoftheMoon",
            "hector678",
            "heiwashin",
            "HelioCentrick",
            "Helio_",
            "helthron",
            "henmo97",
            "heromouse",
            "Hexavolt",
            "higgyl",
            "Hightinker",
            "hollabro",
            "Hollure",
            "HOneYDeWZ17",
            "hotdudelet",
            "hubey2",
            "hudson1",
            "Hudsonhull",
            "huntermhaxx",
            "Iamdead1",
            "icecream26001",
            "ichiimaru77",
            "iCreepersGoBoom",
            "IcySoul",
            "icystealth62",
            "ihelploserface",
            "iKanak",
            "imthelag",
            "inimrepus",
            "Inp_Alive",
            "inuyashaman3",
            "Ipuntbabyz",
            "ironhyde",
            "IrwinKerwin",
            "iSaffy",
            "iScherf",
            "Issac332",
            "iTater",
            "iThe_Disciple",
            "ItzDarklord",
            "IwoveYU",
            "I_Do_It",
            "J0sh23",
            "Jackertud",
            "Jackson223",
            "jacobmorgs",
            "jadenx2000",
            "Jake12121",
            "jake70",
            "JakeKorney",
            "JCDamian10",
            "jcolby99",
            "JCQuiinn",
            "jdididydog",
            "JDSnuff",
            "Jenkks",
            "jennmanahan",
            "jeremysee",
            "jimbo310",
            "JimBouki",
            "Jimboy135",
            "jimfalcon",
            "JimmyTo781",
            "JJ1988",
            "jjd712",
            "jmart85",
            "joejoebeef",
            "joethecrow",
            "joey_dev",
            "joleegirl",
            "Jordan09",
            "Jordycooldude",
            "joshdell2001",
            "Joshua422",
            "Josiah_Farrell",
            "joyjumper",
            "jshreder",
            "jtringl",
            "juniorboyd",
            "JUNKYARD129FAN",
            "justjberg",
            "JustSeth",
            "Just_so_epic",
            "jwbbman",
            "kadenson",
            "kadoro",
            "Kage_of_UF",
            "KainBullet",
            "kaleehma",
            "KaptKang",
            "kasrith",
            "Kassial",
            "Katashi_Ezora",
            "kaylee_ference",
            "kc776",
            "keithr1",
            "Kekens",
            "kell608",
            "kelp07",
            "Ken_McG",
            "kernaleugene",
            "keseyj96",
            "kevinf5",
            "KevinX000",
            "killbot11",
            "killer52698",
            "killerwolfy1",
            "killerzapmaster",
            "kimsoroxlol",
            "KindaSpacey",
            "kingerpy",
            "kingkongsasasin",
            "KingMark",
            "Kingpixels",
            "kiwi6",
            "kkaionsg",
            "KkSoso",
            "klanz",
            "Kleebop",
            "klevin",
            "kley1313",
            "Knowledge519",
            "kokobrainz",
            "KonanXD",
            "Kong999",
            "Konseince",
            "Korblox1134",
            "kornfarmer",
            "Korvic",
            "kotasuperhero",
            "KrayzieJ",
            "KrispyKslave",
            "Krusher548",
            "ktish",
            "ktran253",
            "kumilightbulb",
            "Kumpass_dodo",
            "Kumpass_Skater",
            "kylearmy",
            "L4YT0N",
            "Lachie1020",
            "Lady_Scarlet",
            "Latnem",
            "Lava_soldier",
            "lbvermillion",
            "LDSFlame",
            "Leafreo",
            "Leetskillz",
            "lekin680810",
            "lekrog",
            "lennon8467",
            "LeoFerari",
            "LexiBooHoo",
            "Lgiteco",
            "Lightbikemaster",
            "lilbuddhaman",
            "lilprplebnny",
            "lilymunroe",
            "Lil_Ch00b",
            "limeyman7",
            "lindsayboulanger",
            "LittleFirestar",
            "LittleMidget17",
            "llcyanidell",
            "Lleonidass",
            "Lockewiggen",
            "lodada1",
            "LokiTwoSpirit",
            "Lokthar328i",
            "LOLeannie",
            "Loliotaku",
            "LoneTonberry",
            "LootieLoo",
            "lora_snelson",
            "LordOTheSigns",
            "LoveGunner",
            "LOVETHEWHITE",
            "Lowen_Brau",
            "lucisiac",
            "LuckyChams",
            "M0nstrosity",
            "macmorris92",
            "madon47",
            "MadxRad",
            "mageg",
            "magicman112233",
            "MagmaDragon12",
            "Magyckmage25",
            "mag_wraith",
            "majam",
            "Major_Mayjor",
            "manjiggler",
            "MarcoTheItalian",
            "MarieC90",
            "marshall3128",
            "martinez9",
            "masacardi",
            "masterw3",
            "matharama",
            "matt321123",
            "mattheus02",
            "mattyhastheforce",
            "Maverick_Beast",
            "maxrage",
            "maxxxafterhours",
            "mcarchitect22",
            "McGruber78",
            "McMichael96",
            "mcotterman",
            "mdc0923",
            "MELICENT",
            "meltedcup",
            "mercury199",
            "metalpants2",
            "MetroRoadWarrior",
            "Mewlver82",
            "Mezua",
            "MGBroadcast",
            "mhayes3",
            "midnightchan123",
            "Mikejuju1",
            "mikey14",
            "Mimmy99",
            "MineMyles",
            "miner49burr",
            "minerman183",
            "Miner_Nick_M",
            "Mingpow321",
            "Misterz_Noodles",
            "Mitchsmom",
            "MjCbse",
            "mlp94",
            "mmitchell9",
            "Mobsterguy",
            "ModernDragoness",
            "monkeybidness",
            "MooMooutters",
            "moosehunter123",
            "morgan2041",
            "MortalWombat88",
            "MosesT",
            "motown2003",
            "Mouei",
            "mowat40",
            "mrbaconbitts",
            "MrBlahblah",
            "MrDragonBreath",
            "MrPumpkinMuffins",
            "MrSquirrelMan",
            "Mr_Boomer337",
            "MR_RANDOM999",
            "Mr_SneakyFace",
            "Mr_Sterling",
            "msbrun02",
            "mtndrew1",
            "Muddha",
            "MuffinOfFun",
            "MuffinSpankz",
            "murderrr",
            "murdock99",
            "myers121",
            "mylimo7",
            "myminecraftnow",
            "mysticmock",
            "Mystronghold",
            "M_Boogie_C",
            "nacho30",
            "nanderson17",
            "Narcisism",
            "Navia",
            "nbc0711",
            "NeilYoung",
            "nemaster203",
            "nerdnut",
            "nerraj",
            "nevermines",
            "newmatt003",
            "Nexun",
            "ngennaro",
            "NICFREAK5577",
            "nicilbar",
            "Nick_O_Shlas",
            "Nighthawk354",
            "NightmareV",
            "Nikkisweety",
            "ninjaboy6728",
            "NinjaElite71",
            "nleroy",
            "nobrain98",
            "NoglasticNinja",
            "noisymanray",
            "nolarboot",
            "noob_cat",
            "Noproblembrah",
            "Noresha",
            "nosnarbo",
            "novak189",
            "nr88hg",
            "nubkia",
            "Nyntoku",
            "Nzgbjunior",
            "obfalcons",
            "ObiWanHoshizaki",
            "ogot101",
            "ojpnobeast",
            "oK9power",
            "Old_Ninja",
            "OllyWilliams",
            "oMasterCole",
            "omegasnab1",
            "Omega_Haxors",
            "oOoCodeName2",
            "ooveNoIVIooJR",
            "otic123454321",
            "owning254",
            "ozzyoverlord",
            "Palegreenpants",
            "Pantupino",
            "passiontiger74",
            "Pastabob",
            "patrick60316",
            "patsteirer",
            "payton18",
            "paznos6",
            "pbuelo",
            "PenguinKnight303",
            "permit101",
            "pfcwelch",
            "PhalseFire",
            "Phifft",
            "phil235",
            "philit",
            "PhilVegas",
            "PhreshMatt",
            "Pie2608",
            "pieman35333",
            "pikmin259",
            "pilot23456",
            "PinballWizardxX",
            "Pishe",
            "pjt0620",
            "plaunier",
            "plgxdark",
            "Poedew",
            "Pog_",
            "pomi44",
            "ponderius25",
            "poopypoopman",
            "porky9441",
            "posidanvzeus",
            "possesor1",
            "potassium0",
            "potassiumxthree",
            "POTATO_TOT",
            "Pozzumgee",
            "pres_rufus",
            "Preven",
            "Primordium",
            "prodigy901",
            "prophet19",
            "psychicmuffinpop",
            "pugsy",
            "PunisheR2404",
            "pwn78",
            "qentropy",
            "quinnstrong",
            "Quixoticus",
            "qzr31",
            "R0nBurgandy",
            "Rackshatta",
            "radioactivkitten",
            "Raid4Kill",
            "rakonei",
            "Random_NPC",
            "raphfireball99",
            "Rappin_Cubian",
            "Razgriz_Legend",
            "rcfreek13",
            "rctamura",
            "Reapergirl2",
            "RecycledVomit",
            "reddgirl27",
            "remsat10",
            "RhosID",
            "Rickbox",
            "riskwarlord",
            "ritzfizz",
            "rmckinstry_601",
            "robberbandit",
            "robertsar",
            "RobMayor",
            "Rob_25",
            "roetheverett",
            "rokitchikin",
            "rolf108",
            "roscoepig",
            "Rosdower",
            "Rubber_Duckies",
            "rube99",
            "Ryan6338",
            "ryancup",
            "Ryanorocks",
            "r_ninja14",
            "S4INT",
            "sac3nt3r",
            "SacredAssassin",
            "Sadderday",
            "saggyclamburger",
            "SaintsGamingPUB",
            "samlen",
            "samodexter",
            "Samrith",
            "Samuri_Bake_Pie",
            "sandieman100",
            "SannamWOOT",
            "saromon50",
            "Sasuke7677",
            "sasxxsnip3rxx",
            "Sayomie555",
            "Scarlet_wizard",
            "sccrfreak9",
            "scoobysnakcers",
            "SCORPION222",
            "scottbmx17",
            "scottish1900",
            "Scythe_001",
            "sdog180",
            "seanpr",
            "seaofnarum",
            "sebastianse3",
            "SecureUmbra",
            "Selastrovious",
            "seniorl",
            "SergeantCarew",
            "SergeMonster",
            "SergioXSigala",
            "seth6815",
            "sethallery",
            "Seventhcircle72",
            "sexymamegoma",
            "shadowtype",
            "shadowwarrior567",
            "shaerobbo",
            "Shake_NN_Blake",
            "shane2k3",
            "Sharitoncr18",
            "shoogax12",
            "shooterbooth",
            "Shotsfired",
            "shujaatali",
            "Sicknos",
            "Sidain",
            "Silentcricket",
            "SilverShadeFox",
            "SimplePancake",
            "SimplyGabriele",
            "simsiam",
            "sinisterevil",
            "Sir_Wyvernos",
            "sithlord28",
            "skatefallen",
            "skaterdjdude",
            "sKiLLxSn1p3z",
            "SkronyDog",
            "SkyXI",
            "SLanshe88",
            "Slic3man",
            "slingray120",
            "slong85",
            "Slurth",
            "Smartness",
            "SmDFrylock",
            "smileyface0",
            "Smiley_Riley12",
            "smokingoldenace",
            "SmplstcBllistic",
            "sneakysockpuppet",
            "snipermn12",
            "SnowBirds",
            "soar851",
            "SoDScoper",
            "soldger",
            "Sollomon666",
            "SolomonDeLugo",
            "Soonica",
            "soramiku",
            "Sostratus",
            "Sourful",
            "southsfan123",
            "SOZman794",
            "Spacewolf78",
            "spalo",
            "SpazmaticSniper",
            "SpecOps298",
            "spider333",
            "Spinningbullet",
            "SPIT_skatesnot1",
            "SpottedSnail",
            "sprite8182",
            "spwkiller",
            "spydermonkeyx",
            "SrgWallopy",
            "srs13",
            "Srsbizns",
            "stacey0811",
            "StalkerExecutor",
            "stanglemeir",
            "STaSHZILLA420",
            "SteveTech",
            "Steviewonder3",
            "stiason",
            "stokesinman",
            "Strange_One",
            "Striker211",
            "Sultan_Mogroka",
            "Sunew",
            "sungetsu",
            "Sunnyside193",
            "Sunshine3_14",
            "Sup3rtaco",
            "superdelux",
            "Superdoug",
            "superjohnsonusa",
            "supermedinita",
            "supersizefries",
            "superspike54",
            "superstar277",
            "SupraCl0ne",
            "SurgingRage",
            "susitsu",
            "swiftpaw95",
            "Syrix85",
            "Syronan",
            "systempain",
            "TACOMANPRO",
            "Tacticalspy",
            "tailgater",
            "Tails48685",
            "tankenator1",
            "Tanker808",
            "tardreted",
            "Targus65",
            "Tatekei",
            "TauFirewarrior",
            "Tavorrik",
            "tcassel9898",
            "TehTeNdEnCiEs",
            "tenshispawn",
            "terrorist_109",
            "tesla2000",
            "tgy320",
            "Th1Alchemyst",
            "tharcon",
            "thatspercy",
            "TheAmazingTwix",
            "TheBladerSL",
            "thebrowns",
            "TheCars",
            "thecatman23",
            "theelement6",
            "TheFirstHuman",
            "TheFornicator",
            "TheFunkyBones",
            "TheIMightyPotato",
            "thejamesx00",
            "TheJawn",
            "TheJayPack",
            "TheKoruptor",
            "TheM1NER",
            "Themanq57",
            "TheMightyAnonym",
            "thePJ1",
            "TheRealForte",
            "theSedg",
            "TheSexyLegend",
            "TheSwiftLegend",
            "TheTemplar10",
            "TheVoice22",
            "TheWargy",
            "The_Blind_Seer",
            "The_Red_Ranger",
            "The_Spark",
            "Thunder_Dragon",
            "Thyle",
            "Tifer",
            "Tigershark123",
            "Timewaster8",
            "timmeh5",
            "TinglerTank",
            "Tino_the_Ninja",
            "tinypillow",
            "Tippythebomber",
            "Titra",
            "tlunarrune",
            "Tmartin46",
            "tomallsop16",
            "tombomb60",
            "tonicscientist",
            "TooFat2Camp",
            "toonman4003",
            "torval001",
            "Tosiek12",
            "Toxicblood1",
            "ToXiC_HD",
            "Toxic__Waste",
            "trist990",
            "tronbike",
            "troyt16",
            "ttomss",
            "tubby1147",
            "Turbocharger444",
            "TURTLEINATOR",
            "Turtle_Ball",
            "Tuskor",
            "twelfe",
            "Twinnstars",
            "twinsfan132",
            "TwistedLilly1",
            "twm14",
            "tyroneguy25",
            "TySchauman",
            "Tythus",
            "u000893",
            "ug0ttank3d",
            "uiyot",
            "ultimateowner56",
            "Undead623",
            "Ungainly",
            "valdemarian",
            "valueablesauce",
            "Vandraad",
            "Vast22",
            "veedubrunner",
            "verby30",
            "VIL1917",
            "vilas_win",
            "VileFoxx",
            "vinshanelaur",
            "Vokarius",
            "volcan8bit",
            "VonAulus",
            "vortex1144",
            "w4rl0",
            "wackoman6789",
            "Wanderton",
            "wasaaaaaaa",
            "wb1985",
            "websternet",
            "Weeberlore",
            "wellonzk",
            "WheresMyGreenTea",
            "whiskers20",
            "whiteknight09",
            "whitesoxnumber1",
            "White_Wax",
            "WhizBeatz_069",
            "widget68",
            "Wildwes",
            "wilkawesome",
            "wilkitom",
            "william08567",
            "Willothepro",
            "WillYlliW",
            "WingWang17",
            "winniekawaii",
            "WippitGuud",
            "wirdo",
            "wolfdog54",
            "wolfi96",
            "WolfiyDirewolf",
            "Wolfkrann",
            "wolfscar848",
            "wugytaiw",
            "wVPiPVw",
            "X4Xerum",
            "XBalinoX",
            "xchrisx1234",
            "XDarkness52",
            "xDiablox",
            "xdux",
            "xelfarcherdv",
            "Xelta",
            "xepte",
            "Xerofluff",
            "xers",
            "XeSiphus",
            "xHeraclesx",
            "xiNOVAx",
            "xJoyride",
            "xKennyz",
            "XLR8MooP",
            "xMakaha",
            "xminimuddawgx",
            "xoJulia",
            "xshadows73",
            "XxAA_34xX",
            "XxGayStylexX",
            "XxL3GiTSkULLzxX7",
            "xXMrAkatoshXx",
            "xXNavySealXx",
            "xxswitchblade1",
            "xxtylorramosxx",
            "xxxthe6345Txxx",
            "xXZhuriXx",
            "Xx_AnGrYfEtUs_xX",
            "Y17TraumaHarness",
            "yankees52",
            "Yapper456",
            "YnotMizuka",
            "yomikokun",
            "Yukomaru",
            "Yungeinstein",
            "Yuukke",
            "YvAd123",
            "Z3US1998",
            "zach122167",
            "ZachLegoManiac",
            "zack9789",
            "Zackqack",
            "ZARNAK",
            "zeb516",
            "ZeeLostGhost",
            "zejewbear",
            "Zephar",
            "Zertamis",
            "zeus_of_the_sky",
            "Ze_Major",
            "Zionia000",
            "zmoneyfresh24",
            "ZomgPig",
            "Zorching",
            "zRations",
            "Zunder22",
            "Zxanth",
            "Zzerling",
            "zzeuz",
            "_Insertname_",
            "_pync_",
            "_Shoi_",
            "_Smileyy_",
            "_tever123"
    };
}