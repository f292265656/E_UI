package chen.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppInfo
{
	private static String TAG="AppInfo";

	private Context context;
	private ArrayList<Info> appInfList;
	
	
	private List<PackageInfo> packageInfList;

	public AppInfo(Context context)
	{
       this.context=context;
       appInfList=new ArrayList<AppInfo.Info>();
	}
	public ArrayList<Info> getInfList()
	{
		return appInfList;
	}

	// ��ð�װӦ����Ϣ
	public void loadInfo()
	{

		PackageManager pm = context.getPackageManager();
		packageInfList = pm.getInstalledPackages(0);
		for (int i = 0; i < packageInfList.size(); i++)
		{
			PackageInfo pkInfo = packageInfList.get(i);
			if ((pkInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
			{
				Info tmpAppInfo = new Info();
				tmpAppInfo.app_name = pkInfo.applicationInfo.loadLabel(pm)
						.toString();
				tmpAppInfo.app_icon = pkInfo.applicationInfo.loadIcon(pm);
				tmpAppInfo.packagename=pkInfo.packageName;
		
				appInfList.add(tmpAppInfo);
			}
		}
		Log.i(TAG, "appInfoList is " + appInfList.size() + "\n");

	}
	public ArrayList<HashMap<String, Object>> getSimpleAllAppInf()
	{
		ArrayList<HashMap<String, Object>> simAllAppInf=new ArrayList<HashMap<String,Object>>();
		
		for(Info inf: getInfList())
		{
	       HashMap<String, Object> map=new HashMap<String, Object>();
	       map.put("app_name", inf.app_name);
	       map.put("app_icon", inf.app_icon);
	       simAllAppInf.add(map);
		}
		return simAllAppInf;
	}

	public class Info
	{
		// �汾
		public int versionCode;
		public String versionName;
		// ����
		public String app_name;
		// ��
		public String packagename;
		 //Ӧ�õ���activity��
        String mainActivityName ;

		// ͼ��
		public Drawable app_icon = null;
	}

}
