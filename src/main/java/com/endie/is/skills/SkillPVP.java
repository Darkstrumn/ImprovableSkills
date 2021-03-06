package com.endie.is.skills;

import com.endie.is.InfoIS;
import com.endie.is.api.PlayerSkillBase;
import com.endie.is.api.PlayerSkillData;

public class SkillPVP extends PlayerSkillBase
{
	public SkillPVP()
	{
		super(20);
		setRegistryName(InfoIS.MOD_ID, "pvp");
	}
	
	@Override
	public int getXPToUpgrade(PlayerSkillData data, short targetLvl)
	{
		return (int) Math.pow(targetLvl, 2);
	}
}