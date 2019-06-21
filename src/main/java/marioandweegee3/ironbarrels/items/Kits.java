package marioandweegee3.ironbarrels.items;

import java.util.Map;

import com.google.common.collect.Maps;

import marioandweegee3.ironbarrels.IronBarrels;
import marioandweegee3.ironbarrels.blocks.Barrels;

public class Kits{
    public static void init(){
        Map<Integer, String> tierMap = Maps.newHashMap();

        int count = 0;
        for(String mat : Barrels.materials){
            tierMap.put(count, mat);
            count++;
        }

        for(Map.Entry<Integer, String> fromEntry : tierMap.entrySet()){
            for(Map.Entry<Integer, String> toEntry : tierMap.entrySet()){
                if(fromEntry.getKey() < toEntry.getKey()){
                    registerKit(BarrelConversionKit.create(fromEntry.getValue()+"_barrel", toEntry.getValue()+"_barrel"), makeKitName(fromEntry.getValue(), toEntry.getValue()));
                }
            }
        }
    }

    private static String makeKitName(String from, String to){
        return from + "_" + to + "_kit";
    }

    public static void registerKit(BarrelConversionKit kit, String name){
        IronBarrels.register(kit, name);
    }
}