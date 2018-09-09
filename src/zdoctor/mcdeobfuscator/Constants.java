package zdoctor.mcdeobfuscator;

public class Constants {
	public static enum MapType {
		Daily, Stable, Live, Custom, Obf
	}

	public static final String VERSION = "3.1";

	public static final String DEFAULT_SEARCH = "- Search Text -";
	public static final String ILLEGAL_CHAR = "[\\'\\\"*!@#%^&*()\"{}\\]\\[|\\?<>\\\\,. ;]";

	public static final String STABLE_JSON = "stable";
	public static final String DAILY_JSON = "snapshot";

	protected static final String VERSIONS_JSON = "http://export.mcpbot.bspk.rs/versions.json";
	protected static final String SRG = "mcp-%s-srg.zip";
	protected static final String SRGURL = "http://export.mcpbot.bspk.rs/mcp/%s/" + SRG;
	// SnapShot
	protected static final String DAILY = "mcp_snapshot-%d-%s.zip";
	protected static final String DAILYURL = "http://export.mcpbot.bspk.rs/mcp_snapshot/%d-%s/" + DAILY;

	protected static final String STABLES = "mcp_stable-%d-%s.zip";
	protected static final String STABLESURL = "http://export.mcpbot.bspk.rs/mcp_stable/%d-%s/" + STABLES;

	public static final String CACHE_FOLDER = "cache";
	public static final String VERSION_JSON_FILE = "versions.json";

	public static final String LIVE_FIELDS = "http://export.mcpbot.bspk.rs/fields.csv";
	public static final String LIVE_METHODS = "http://export.mcpbot.bspk.rs/methods.csv";
	public static final String LIVE_PARAMS = "http://export.mcpbot.bspk.rs/params.csv";

	public static final String LIVE_ZIP = "Semi-Live.zip";

	public static final String LOG = "Parsed '%d' files and replaced '%d' entries in '%.2f' seconds";

	public static final String SRG_Entry = "joined.srg";
	public static final String SRG_PACKAGE = "PK:";
	public static final String SRG_CLASS_LOADER = "CL:";
	public static final String SRG_FIELD = "FD:";
	public static final String SRG_METHOD = "MD:";

	
}
