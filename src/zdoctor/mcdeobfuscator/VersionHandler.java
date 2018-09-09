package zdoctor.mcdeobfuscator;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import zdoctor.commons.utils.DebugUtil;
import zdoctor.commons.utils.WebDownloader;
import zdoctor.commons.utils.data.FileUtil;
import zdoctor.mcdeobfuscator.Constants.MapType;

public class VersionHandler {
	public static final HashMap<String, HashMap<Constants.MapType, ArrayList<Long>>> MAPPINGS = new HashMap<>();

	public static Object downloadVersionJson() {
		try {
			byte[] versionJsonData = WebDownloader.downloadToArray(Constants.VERSIONS_JSON);

			if (versionJsonData.length <= 0)
				return null;

			FileUtil.flushToFile(Constants.CACHE_FOLDER + "/" + Constants.VERSION_JSON_FILE, versionJsonData);
			return new JSONParser().parse(new String(versionJsonData));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object loadCachedVersionJson() {
		try {
			File versionJson = new File(Constants.CACHE_FOLDER + "/" + Constants.VERSION_JSON_FILE);

			if (versionJson.exists()) {
				byte[] versionJsonData = FileUtil.getFileData(versionJson);
				if (versionJsonData.length <= 0)
					return null;
				return new JSONParser().parse(new String(versionJsonData));
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	public static Object loadBuiltInVersionJson() {
		try {
			return new JSONParser().parse(new InputStreamReader(
					VersionHandler.class.getClassLoader().getResourceAsStream("zdoctor/resources/versions.json")));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void reload() {
		Object obj = downloadVersionJson();

		if (obj == null)
			obj = loadCachedVersionJson();

		if (obj == null)
			obj = loadBuiltInVersionJson();

		if (obj == null) {
			DebugUtil.notifyUser("Unable to load any mappings");
			System.exit(-1);
		}

		JSONObject jsonVersion = (JSONObject) obj;

		Iterator<?> versionData = jsonVersion.keySet().iterator();

		while (versionData.hasNext()) {
			String mcVersion = (String) versionData.next();
			HashMap<Constants.MapType, ArrayList<Long>> mappings = new HashMap<>();
			ArrayList<Long> stableVersions = new ArrayList<>();
			ArrayList<Long> dailyVersions = new ArrayList<>();

			JSONObject jsonMappingType = (JSONObject) jsonVersion.get(mcVersion);
			Iterator<?> mappingTypes = jsonMappingType.keySet().iterator();
			while (mappingTypes.hasNext()) {
				String mappingType = (String) mappingTypes.next();
				JSONArray jsonMappings = (JSONArray) jsonMappingType.get(mappingType);
				Iterator<?> mappingsItr = jsonMappings.iterator();
				while (mappingsItr.hasNext()) {
					Long mapping = (Long) mappingsItr.next();
					switch (mappingType.toLowerCase()) {
					case Constants.STABLE_JSON:
						stableVersions.add(mapping);
						break;
					case Constants.DAILY_JSON:
						dailyVersions.add(mapping);
						break;
					default:
						break;
					}
				}
			}

			mappings.put(MapType.Stable, stableVersions);
			mappings.put(MapType.Daily, dailyVersions);
			mappings.put(MapType.Obf, dailyVersions);
			MAPPINGS.put(mcVersion, mappings);
		}
	}

	public static String getDailyZip(String mcVersion, long mapping) {
		return String.format(Constants.DAILY, mapping, mcVersion);
	}

	public static String getDailyURL(String mcVersion, long mapping) {
		return String.format(Constants.DAILYURL, mapping, mcVersion, mapping, mcVersion);
	}

	public static String getStableZip(String mcVersion, long mapping) {
		return String.format(Constants.STABLES, mapping, mcVersion);
	}

	public static String getStableURL(String mcVersion, long mapping) {
		return String.format(Constants.STABLESURL, mapping, mcVersion, mapping, mcVersion);
	}

	public static String getSRGZip(String mcVersion) {
		return String.format(Constants.SRG, mcVersion);
	}

	public static String getSRGURL(String mcVersion) {
		return String.format(Constants.SRGURL, mcVersion, mcVersion);
	}

}
