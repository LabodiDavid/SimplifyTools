package hu.ditservices.utils;

import hu.ditservices.STPlugin;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Method;
import java.util.List;

public class AdvancementHelper {
    private final STPlugin plugin = STPlugin.getInstance();
    private final FileConfiguration config;

    private boolean initialized = false;

    public AdvancementHelper(FileConfiguration c){
        this.config = c;
        this.initialize();
    }
    private Integer type;

    public String[][] normal = {
            {"story/mine_stone", "Stone Age"},
            {"story/upgrade_tools", "Getting an Upgrade"},
            {"story/smelt_iron", "Acquire Hardware"},
            {"story/obtain_armor", "Suit Up"},
            {"story/lava_bucket", "Hot Stuff"},
            {"story/iron_tools", "Isn't It Iron Pick"},
            {"story/deflect_arrow", "Not Today, Thank You"},
            {"story/form_obsidian", "Ice Bucket Challenge"},
            {"story/mine_diamond", "Diamonds!"},
            {"story/enter_the_nether", "We Need to Go Deeper"},
            {"story/shiny_gear", "Cover Me With Diamonds"},
            {"story/enchant_item", "Enchanter"},
            {"story/follow_ender_eye", "Eye Spy"},
            {"story/enter_the_end", "The End?"},
            {"nether/find_fortress", "A Terrible Fortress"},
            {"nether/get_wither_skull", "Spooky Scary Skeleton"},
            {"nether/obtain_blaze_rod", "Into Fire"},
            {"nether/summon_wither", "Withering Heights"},
            {"nether/brew_potion", "Local Brewery"},
            {"nether/create_beacon", "Bring Home the Beacon"},
            {"nether/find_bastion", "Those Were the Days"},
            {"nether/obtain_ancient_debris", "Hidden in the Depths"},
            {"nether/obtain_crying_obsidian", "Who is Cutting Onions?"},
            {"nether/distract_piglin", "Oh Shiny"},
            {"nether/ride_strider", "This Boat Has Legs"},
            {"nether/loot_bastion", "War Pigs"},
            {"nether/use_lodestone", "Country Lode, Take Me Home"},
            {"nether/charge_respawn_anchor", "Not Quite \"Nine\" Lives"},
            {"end/kill_dragon", "Free the End"},
            {"end/enter_end_gateway", "Remote Getaway"},
            {"end/find_end_city", "The City at the End of the Game"},
            {"adventure/voluntary_exile", "Voluntary Exile"},
            {"adventure/kill_a_mob", "Monster Hunter"},
            {"adventure/trade", "What a Deal!"},
            {"adventure/honey_block_slide", "Sticky Situation"},
            {"adventure/ol_betsy", "Ol' Betsy"},
            {"adventure/sleep_in_bed", "Sweet Dreams"},
            {"adventure/throw_trident", "A Throwaway Joke"},
            {"adventure/shoot_arrow", "Take Aim"},
            {"adventure/whos_the_pillager_now", "Who's the Pillager Now?"},
            {"adventure/very_very_frightening", "Very Very Frightening"},
            {"husbandry/safely_harvest_honey", "Bee Our Guest"},
            {"husbandry/breed_an_animal", "The Parrots and the Bats"},
            {"husbandry/tame_an_animal", "Best Friends Forever"},
            {"husbandry/fishy_business", "Fishy Business"},
            {"husbandry/silk_touch_nest", "Total Beelocation"},
            {"husbandry/plant_seed", "A Seedy Place"},
            {"husbandry/tactical_fishing", "Tactical Fishing"}
    };
    public String[][] goal = {
            {"story/cure_zombie_villager", "Zombie Doctor"},
            {"nether/create_full_beacon", "Beaconator"},
            {"end/dragon_egg", "The Next Generation"},
            {"end/respawn_dragon", "The End... Again..."},
            {"end/dragon_breath", "You Need a Mint"},
            {"end/elytra", "Sky's the Limit"},
            {"adventure/totem_of_undying", "Postmortal"},
            {"adventure/summon_iron_golem", "Hired Help"}
    };
    public String[][] challenge = {
            {"nether/return_to_sender", "Return to Sender"},
            {"nether/fast_travel", "Subspace Bubble"},
            {"nether/uneasy_alliance", "Uneasy Alliance"},
            {"nether/all_potions", "A Furious Cocktail"},
            {"nether/all_effects", "How Did We Get Here?"},
            {"nether/netherite_armor", "Cover Me in Debris"},
            {"nether/explore_nether", "Hot Tourist Destinations"},
            {"end/levitate", "Great View From Up Here"},
            {"adventure/hero_of_the_village", "Hero of the Village"},
            {"adventure/kill_all_mobs", "Monsters Hunted"},
            {"adventure/two_birds_one_arrow", "Two Birds, One Arrow"},
            {"adventure/arbalistic", "Arbalistic"},
            {"adventure/adventuring_time", "Adventuring Time"},
            {"adventure/sniper_duel", "Sniper Duel"},
            {"adventure/bullseye", "Bullseye"},
            {"husbandry/bred_all_animals", "Two by Two"},
            {"husbandry/complete_catalogue", "A Complete Catalogue"},
            {"husbandry/balanced_diet", "A Balanced Diet"},
            {"husbandry/break_diamond_hoe", "Serious Dedication"},
            {"husbandry/obtain_netherite_hoe", "Serious Dedication"},
    };
    public String[] out_type = new String[3];

    private void initialize(){

        if (config.isSet("CustomAdvancement.formats.advancement")) {
            out_type[0] = config.getString("CustomAdvancement.formats.advancement");
        }
        if (config.isSet("CustomAdvancement.formats.goal")) {
            out_type[1] = config.getString("CustomAdvancement.formats.goal");
        }
        if (config.isSet("CustomAdvancement.formats.challenge")) {
            out_type[2] = config.getString("CustomAdvancement.formats.challenge");
        }
        String disablemsg = plugin.getPrefix()+"Disabling vanilla advancement messages for ";
        List<World> worlds = this.plugin.getServer().getWorlds();
        Method setRuleMethod, getRuleMethod;
        try {
                for (World w : worlds) {
                    if (Version.ServerVersion.isCurrentEqualOrLower(Version.ServerVersion.v1_12_R1)){
                        getRuleMethod = w.getClass().getDeclaredMethod("getGameRuleValue", String.class);
                        if (getRuleMethod.invoke(w,"announceAdvancements")=="true"){
                            setRuleMethod = w.getClass().getDeclaredMethod("setGameRuleValue", String.class, String.class);
                            setRuleMethod.invoke(w,"announceAdvancements", "false");
                            this.plugin.getLogger().info(disablemsg + w.getName());
                        }
                    }else{
                        getRuleMethod = w.getClass().getDeclaredMethod("getGameRuleValue", GameRule.class);
                        if ((boolean)getRuleMethod.invoke(w,GameRule.ANNOUNCE_ADVANCEMENTS)){
                            setRuleMethod = w.getClass().getDeclaredMethod("setGameRule", GameRule.class, Object.class);
                            setRuleMethod.invoke(w,GameRule.ANNOUNCE_ADVANCEMENTS,false);
                            this.plugin.getLogger().info(disablemsg + w.getName());
                        }

                    }
                    //w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
                }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getText(String PlayerName,String title){
        String rtrn = this.out_type[this.type];
        rtrn = rtrn.replace("{NAME}", PlayerName).replace("{ADV}", title);

        return rtrn;
    }
    public String find(String ad) {
        for (int i = 0; i < normal.length; i++)
            if (ad.equals(normal[i][0])) {
                type = 0;
                return normal[i][1];
            }
        for (int i = 0; i < goal.length; i++)
            if (ad.equals(goal[i][0])) {
                type = 1;
                return goal[i][1];
            }
        for (int i = 0; i < challenge.length; i++)
            if (ad.equals(challenge[i][0])) {
                type = 2;
                return challenge[i][1];
            }
        return ad;
    }

    public boolean check(String adv) {
        if(adv.contains("root") || adv.contains("recipes"))
            return false;
        else
            return true;
    }


}
