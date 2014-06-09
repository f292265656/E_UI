package chen.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.app.Fragment;
import android.util.Log;

public class Plugins
{
	private static String TAG="PluginsInf";
	private Context contetx;
	// --------------new
	private ArrayList<PluginInf> pluginInfList;
	private PackageManager pm;
	public static Class pluginClass;
	public static Object pluginObject;
	
	public Plugins(Context context)
	{
		this.contetx = context;
		pluginInfList = new ArrayList<PluginInf>();
		pm = contetx.getPackageManager();
	}
	public void searchPlugins()
	{
		
			pluginInfList.clear();
		
		
		List<PackageInfo> pkgs = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		
		
		for (PackageInfo pkg : pkgs)
		{
            String pacgeName=pkg.packageName;
            String sharedUserId=pkg.sharedUserId;
        
            if(!"com.eteam.e_ui".equals(sharedUserId) || "com.eteam.e_ui".equals(pacgeName))
            	continue;
            
            //装载信息
            PluginInf inf=new PluginInf();
            inf.lable=pm.getApplicationLabel(pkg.applicationInfo).toString();
            inf.packageName=pacgeName;
           // inf.icon=pm.getApplicationIcon(pkg.applicationInfo)pkg.
            inf.fragement=getFragMent(inf.packageName);
            pluginInfList.add(inf);
            
		}
	}
	public  ArrayList<PluginInf> getPluginInflist()
	{
		return pluginInfList;
	}
	

	private Fragment getFragMent(String packName)
	{
		Fragment f = null;
		//反射获取插件（Fragement）
		
		try
		{
			Context plguginContext=contetx.createPackageContext(packName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
			pluginClass=plguginContext.getClassLoader().loadClass(packName+".PluginDes");
			Class[] argType=new Class[]{Context.class,Context.class};
			Object[] agrParam=new Object[]{contetx,plguginContext};
			Constructor constructor=pluginClass.getDeclaredConstructor(argType);
			constructor.setAccessible(true);
			pluginObject=constructor.newInstance(agrParam);
			Method m=pluginClass.getDeclaredMethod("getPlugin");
			 f=(Fragment) m.invoke(pluginObject);
			Log.i(TAG, "Fragment is "+f.toString());
		    
			
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NameNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return f;
	}

}
