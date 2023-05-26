package com.bitlab.game.booster.gfx.tool.DocHandling;

public class UserCustom {

    public static String getResolution(int resolution){
        switch(resolution){
            case 1: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4449574F49"; //144p
            case 2: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4449574F49"; //360p
            case 3: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4449574E4C"; //480p
            case 4: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B444957414C"; //540p
            case 5: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4449574049"; //640p
            case 7: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4448574D49"; //1080p
            case 8: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4448574D4D"; //1080p FHD+
            case 9: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4448574D40"; //1440p
                default: return "+CVars=0B5734161B10151C3A16170D1C170D2A1A18151C3F181A0D160B4448"; //720p
        }

    }

    public static String getFPS(int fps){
        switch (fps){
            case 1: return "+CVars=0B57292C3B3E3D1C0F101A1C3F292A35160E444A49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A34101D444A49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A31101E11444A49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A313D2B444A49"; //30FPS

            case 2: return "+CVars=0B57292C3B3E3D1C0F101A1C3F292A35160E444D49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A34101D444D49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A31101E11444D49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A313D2B444D49"; //40FPS

            /*case 4|5: return "+CVars=0B57292C3B3E3D1C0F101A1C3F292A35160E444049\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A34101D444049\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A31101E11444049\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A313D2B444049"; //90FPS - 120FPS*/

            default: return "+CVars=0B57292C3B3E3D1C0F101A1C3F292A35160E444F49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A34101D444F49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A31101E11444F49\n" +
                    "+CVars=0B57292C3B3E3D1C0F101A1C3F292A313D2B444F49"; //60FPS
        }
    }

    public static String getGraphics(int graphics){

        switch(graphics){
            case 0: return "+CVars=0B572C0A1C0B280C1815100D002A1C0D0D10171E4449\n" +
                            "+CVars=0B5734161B10151C313D2B44495749"; //Smooth
            case 1: return "+CVars=0B572C0A1C0B280C1815100D002A1C0D0D10171E4448"; //Medium
            case 2: return "+CVars=0B572C0A1C0B280C1815100D002A1C0D0D10171E444B"; //HD
            default: return "+CVars=0B572C0A1C0B280C1815100D002A1C0D0D10171E444A"; //HDR
        }

    }

    public static String getStyle(int style){

        switch(style){
            case 2: return "+CVars=0B572C0A1C0B313D2B2A1C0D0D10171E444B";
            case 3: return "+CVars=0B572C0A1C0B313D2B2A1C0D0D10171E444A";
            case 4: return "+CVars=0B572C0A1C0B313D2B2A1C0D0D10171E444D";
            case 5: return "+CVars=0B572C0A1C0B313D2B2A1C0D0D10171E444C";
            case 6: return "+CVars=0B572C0A1C0B313D2B2A1C0D0D10171E444F";
            default: return "+CVars=0B572C0A1C0B313D2B2A1C0D0D10171E4448";
        }

    }

    public static String getShadowQuality(int shadow){
        switch (shadow){
            case 0: return "+CVars=0B572A11181D160E280C1815100D004449";
            default: return "+CVars=0B572A11181D160E280C1815100D004448";
        }
    }

    public static String getDetailMode(int mode){
        switch (mode){
            case 0: return "+CVars=0B573D1C0D18101534161D1C4449";
            case 1: return "+CVars=0B573D1C0D18101534161D1C4448";
            default: return "+CVars=0B573D1C0D18101534161D1C444B";
        }
    }

    public static String getBlur(int blur){
        switch(blur){
            case 1: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=95";
            case 3: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=70";
            case 4: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=60";
            case 5: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=55";
            case 6: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=50";
            case 7: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=40";
            case 8: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=30";
            default: return "[ScalabilityGroups]\n" +
                    "Sg.ResolutionQuality=75";
        }
    }

    public static String getOtherBlur(){
        return "Sg.AntiAliasingQuality=0\n" +
                "Sg.ShadowQuality=0\n" +
                "Sg.PostProcessQuality=0\n" +
                "Sg.TextureQuality=0\n" +
                "Sg.EffectQuality=0\n" +
                "Sg.FoliageQuality=0\n" +
                "Sg.TrueSkyQuality=0\n" +
                "Sg.GroundClutterQuality=0\n" +
                "Sg.IBLQualuty=0\n" +
                "Sg.PUBGQualutyLv1=0";
    }

    public static String getOtherGameUserSettings(){
        return "sg.ViewDistanceQuality=0\n" +
                "sg.AntiAliasingQuality=0\n" +
                "sg.ShadowQuality=0\n" +
                "sg.PostProcessQuality=0\n" +
                "sg.TextureQuality=0\n" +
                "sg.EffectsQuality=0\n" +
                "sg.FoliageQuality=0\n" +
                "sg.ShadingQuality=0\n" +
                "\n" +
                "[/Script/Engine.GameUserSettings]\n" +
                "bUseDynamicResolution=True\n" +
                "FrameRateLimit=EXTREME\n" +
                "\n" +
                "[ShaderPipelineCache.CacheFile]\n" +
                "LastOpened=Extreme\n" +
                "\n" +
                "[/script/engine.renderersettings]\n" +
                "r.SeparateTranslucency=False\n" +
                "r.CustomDepth=0\n" +
                "r.DefaultFeature.Bloom=0\n" +
                "r.DefaultFeature.AmbientOcclusion=0\n" +
                "r.DefaultFeature.AmbientOcclusionStaticFraction=0\n" +
                "r.DefaultFeature.MotionBlur=0\n" +
                "r.DefaultFeature.LensFlare=0\n" +
                "r.DefaultSinzuGaming=0\n" +
                "r.DefaultFeature.AntiAliasing=0\n" +
                "r.ViewDistanceScale=0\n" +
                "r.ShadowQuality=0\n" +
                "r.Shadow.CSM.MaxCascades=0\n" +
                "r.Shadow.MaxResolution=0\n" +
                "r.Shadow.RadiusThreshold=0";
    }

}
