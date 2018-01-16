package org.golde.java.discordbotapi;

import sx.blah.discord.handle.obj.StatusType;

public enum EnumStatus {
	ONLINE(StatusType.ONLINE), AWAY(StatusType.IDLE), DND(StatusType.DND), INVISIBLE(StatusType.INVISIBLE);
	public final StatusType dcs;
	EnumStatus(StatusType dcs){
		this.dcs = dcs;
	}
}
