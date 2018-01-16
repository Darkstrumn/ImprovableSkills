package com.endie.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.endie.is.api.PlayerSkillBase;
import com.endie.is.data.PlayerDataManager;
import com.endie.is.init.ItemsIS;
import com.endie.is.init.SkillsIS;
import com.endie.is.proxy.CommonProxy;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.intent.IntentManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(modid = InfoIS.MOD_ID, name = InfoIS.MOD_NAME, version = InfoIS.MOD_VERSION, dependencies = "required-after:hammercore")
public class ImprovableSkillsMod
{
	@Instance
	public static ImprovableSkillsMod instance;
	
	@SidedProxy(clientSide = "com.endie.is.proxy.ClientProxy", serverSide = "com.endie.is.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static final Logger LOG = LogManager.getLogger(InfoIS.MOD_NAME);
	
	@EventHandler
	public void construct(FMLConstructionEvent evt)
	{
		IForgeRegistry<PlayerSkillBase> reg = new RegistryBuilder<PlayerSkillBase>().setName(new ResourceLocation(InfoIS.MOD_ID, "stats")).setType(PlayerSkillBase.class).create();
		
		IntentManager.registerIntentHandler(InfoIS.MOD_ID + ".getData", EntityPlayer.class, (mod, data) -> PlayerDataManager.getDataFor(data));
		IntentManager.registerIntentHandler(InfoIS.MOD_ID + ".save", EntityPlayer.class, (mod, data) -> PlayerDataManager.save(data));
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		SimpleRegistration.registerFieldItemsFrom(ItemsIS.class, InfoIS.MOD_ID, CreativeTabs.MISC);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init();
	}
	
	@SubscribeEvent
	public void addRecipes(RegistryEvent.Register<IRecipe> e)
	{
		IForgeRegistry<IRecipe> reg = e.getRegistry();
		
		LOG.info("RegistryEvent.Register<IRecipe>");
		reg.register(SimpleRegistration.parseShapedRecipe(new ItemStack(ItemsIS.SKILLS_BOOK), "lbl", "pgp", "lbl", 'l', "leather", 'b', Items.BOOK, 'p', "paper", 'g', "ingotGold").setRegistryName(InfoIS.MOD_ID, "skills_book"));
	}
	
	@SubscribeEvent
	public void addStats(RegistryEvent.Register<PlayerSkillBase> e)
	{
		LOG.info("RegistryEvent.Register<PlayerSkillBase>");
		SkillsIS.register(e.getRegistry());
	}
	
	@SubscribeEvent
	public void playerJoin(PlayerLoggedInEvent e)
	{
		PlayerDataManager.loadLogging(e.player);
	}
	
	@SubscribeEvent
	public void playerLeft(PlayerLoggedOutEvent e)
	{
		PlayerDataManager.saveQuitting(e.player);
	}
}