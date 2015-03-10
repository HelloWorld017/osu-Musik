package org.khinenw.osu.musik;

public enum SettingReference {
	osudir("osu! Songs folder", AvailableSettingType.folder),
	shuffle("Shuffle", AvailableSettingType.bool), 
	showcurrent("Show current playing", AvailableSettingType.bool);
	
	public String title;
	public AvailableSettingType type;
	
	private SettingReference(String title, AvailableSettingType type){
		this.title = title;
		this.type = type;
	}
}
