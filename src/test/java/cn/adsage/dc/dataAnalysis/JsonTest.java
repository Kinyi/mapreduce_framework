package cn.adsage.dc.dataAnalysis;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONObject;
import org.junit.Test;

public class JsonTest {

	@Test
	public void test() {
//		String json = "{\"ip\":{\"192.168.1.1\":\"1000\"},\"version\":{\"5s\":\"5000\"},\"type\":\"full\"}";
		/*String json = "{\"ip\":{\"192.168.1.1\":\"1000\",\"192.168.1.171\":\"2000\",\"192.168.1.255\":\"3000\"},\"version\":{\"5s\":\"5000\",\"4s\":\"6000\"}}";
		
		System.out.println(Jsonutils.parseTwoNoNestedAttr(json, "ip"));*/
		
		/*JSONObject jsonObject = new JSONObject(json);
		Set keySet = jsonObject.keySet();
		for (Object key : keySet) {
			if (key instanceof String) {
				key = (String)key;
			}
			System.out.println(key);
		}*/
		
		/*JSONObject jsonObject = new JSONObject(json);
		String typestring = jsonObject.getString("type");
		System.out.println(typestring);
		JSONObject ipObject = jsonObject.getJSONObject("ip");
		JSONObject vObject = jsonObject.getJSONObject("version");
		String iptimes = ipObject.getString("192.168.1.1");
		String vtimes = vObject.getString("5s");
		System.out.println(iptimes);
		System.out.println(vtimes);
		
		JSONObject jobject = new JSONObject();
		jobject.put("192.168.1.1", "0.1");
		jobject.put("192.168.1.172", "0.2");
		JSONObject ipobject = new JSONObject();
		ipobject.put("ip", jobject);
		String string = ipobject.toString();
		System.out.println(string);*/
		
		
		/*System.out.println(Jsonutils.parseOneJsonObj(json, "type"));
		System.out.println(Jsonutils.parseTwoJsonObj(json, "ip", "192.168.1.1"));
		System.out.println(Jsonutils.parseTwoJsonObj(json, "version", "5s"));*/
		
		
		/*HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("5s", "0.6");
		hashMap.put("4s", "0.3");
		System.out.println(Jsonutils.generateJson("version", hashMap));
		*/
		
		/*String num = "1000";
		String frequence = Float.parseFloat(num)/10000+"";
		System.out.println(frequence);*/
		
		/*String content = "abc \t".trim();
		System.out.println(content);*/
		
		String content = "abc,";
		String[] split = content.split(",");
		System.out.println(split.length);
	}

}
