package no.srib.sribapp.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the podcast database table.
 * 
 */
@Entity
@Table(name="podcast")
@NamedQuery(name="Podcast.findAll", query="SELECT p FROM Podcast p")
public class Podcast implements Serializable {
	private static final long serialVersionUID = 1L;
	private int refnr;
	private int archivedate;
	private int audioformat;
	private int audiomode;
	private String author;
	private int bitrate;
	private String broadcast;
	private int broadcastdate;
	private int broadcastings;
	private int carrier;
	private short cartpriority;
	private int category;
	private int changedate;
	private int changetime;
	private String changeuser;
	private int class_;
	private int createdate;
	private int createtime;
	private String creator;
	private int customer;
	private int deletedate;
	private short donut;
	private int duration;
	private String editor;
	private int endcode;
	private int era;
	private int extdevice;
	private int fadein;
	private int fadeout;
	private int fading;
	private String filename;
	private String filename2;
	private int filesize;
	private int firstusedate;
	private String flags;
	private String foreignmotive;
	private String gemaid;
	private short generation;
	private int identifier;
	private String informat;
	private int instrumentation;
	private int intensity;
	private int intro;
	private String keywords;
	private int language;
	private int lastusedate;
	private int markin;
	private int markout;
	private int maxlevel;
	private int mediumcode;
	private String mediumname;
	private int mediumtype;
	private int mood;
	private String motive;
	private int musicformat;
	private String musicid;
	private String origin;
	private int outro;
	private String personal;
	private int playtime;
	private String pool;
	private int presenter;
	private int priority;
	private int product;
	private int productgroup;
	private int production;
	private int program;
	private int project;
	private short ramp;
	private int recorddate;
	private String registration;
	private String remark;
	private short replflags;
	private String replident;
	private short ressort;
	private int samplerate;
	private int seasonal;
	private int sendrights;
	private short softdel;
	private String source;
	private int speaker;
	private int spotlength;
	private int state;
	private int story;
	private int style;
	private short subressort;
	private short tag;
	private int target;
	private int tempo;
	private String title;
	private int type;
	private int usecount;
	private short weekday;

	public Podcast() {
	}


	@Id
	@Column(unique=true, nullable=false)
	public int getRefnr() {
		return this.refnr;
	}

	public void setRefnr(int refnr) {
		this.refnr = refnr;
	}


	public int getArchivedate() {
		return this.archivedate;
	}

	public void setArchivedate(int archivedate) {
		this.archivedate = archivedate;
	}


	public int getAudioformat() {
		return this.audioformat;
	}

	public void setAudioformat(int audioformat) {
		this.audioformat = audioformat;
	}


	public int getAudiomode() {
		return this.audiomode;
	}

	public void setAudiomode(int audiomode) {
		this.audiomode = audiomode;
	}


	@Column(length=254)
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	public int getBitrate() {
		return this.bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}


	@Column(length=40)
	public String getBroadcast() {
		return this.broadcast;
	}

	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
	}


	public int getBroadcastdate() {
		return this.broadcastdate;
	}

	public void setBroadcastdate(int broadcastdate) {
		this.broadcastdate = broadcastdate;
	}


	public int getBroadcastings() {
		return this.broadcastings;
	}

	public void setBroadcastings(int broadcastings) {
		this.broadcastings = broadcastings;
	}


	public int getCarrier() {
		return this.carrier;
	}

	public void setCarrier(int carrier) {
		this.carrier = carrier;
	}


	public short getCartpriority() {
		return this.cartpriority;
	}

	public void setCartpriority(short cartpriority) {
		this.cartpriority = cartpriority;
	}


	public int getCategory() {
		return this.category;
	}

	public void setCategory(int category) {
		this.category = category;
	}


	public int getChangedate() {
		return this.changedate;
	}

	public void setChangedate(int changedate) {
		this.changedate = changedate;
	}


	public int getChangetime() {
		return this.changetime;
	}

	public void setChangetime(int changetime) {
		this.changetime = changetime;
	}


	@Column(length=10)
	public String getChangeuser() {
		return this.changeuser;
	}

	public void setChangeuser(String changeuser) {
		this.changeuser = changeuser;
	}


	@Column(name="CLASS")
	public int getClass_() {
		return this.class_;
	}

	public void setClass_(int class_) {
		this.class_ = class_;
	}


	public int getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(int createdate) {
		this.createdate = createdate;
	}


	public int getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(int createtime) {
		this.createtime = createtime;
	}


	@Column(length=10)
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}


	public int getCustomer() {
		return this.customer;
	}

	public void setCustomer(int customer) {
		this.customer = customer;
	}


	public int getDeletedate() {
		return this.deletedate;
	}

	public void setDeletedate(int deletedate) {
		this.deletedate = deletedate;
	}


	public short getDonut() {
		return this.donut;
	}

	public void setDonut(short donut) {
		this.donut = donut;
	}


	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}


	@Column(length=254)
	public String getEditor() {
		return this.editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}


	public int getEndcode() {
		return this.endcode;
	}

	public void setEndcode(int endcode) {
		this.endcode = endcode;
	}


	public int getEra() {
		return this.era;
	}

	public void setEra(int era) {
		this.era = era;
	}


	public int getExtdevice() {
		return this.extdevice;
	}

	public void setExtdevice(int extdevice) {
		this.extdevice = extdevice;
	}


	public int getFadein() {
		return this.fadein;
	}

	public void setFadein(int fadein) {
		this.fadein = fadein;
	}


	public int getFadeout() {
		return this.fadeout;
	}

	public void setFadeout(int fadeout) {
		this.fadeout = fadeout;
	}


	public int getFading() {
		return this.fading;
	}

	public void setFading(int fading) {
		this.fading = fading;
	}


	@Column(length=254)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}


	@Column(length=254)
	public String getFilename2() {
		return this.filename2;
	}

	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}


	public int getFilesize() {
		return this.filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}


	public int getFirstusedate() {
		return this.firstusedate;
	}

	public void setFirstusedate(int firstusedate) {
		this.firstusedate = firstusedate;
	}


	@Column(length=20)
	public String getFlags() {
		return this.flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}


	@Column(length=40)
	public String getForeignmotive() {
		return this.foreignmotive;
	}

	public void setForeignmotive(String foreignmotive) {
		this.foreignmotive = foreignmotive;
	}


	@Column(length=20)
	public String getGemaid() {
		return this.gemaid;
	}

	public void setGemaid(String gemaid) {
		this.gemaid = gemaid;
	}


	public short getGeneration() {
		return this.generation;
	}

	public void setGeneration(short generation) {
		this.generation = generation;
	}


	public int getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}


	@Column(length=20)
	public String getInformat() {
		return this.informat;
	}

	public void setInformat(String informat) {
		this.informat = informat;
	}


	public int getInstrumentation() {
		return this.instrumentation;
	}

	public void setInstrumentation(int instrumentation) {
		this.instrumentation = instrumentation;
	}


	public int getIntensity() {
		return this.intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}


	public int getIntro() {
		return this.intro;
	}

	public void setIntro(int intro) {
		this.intro = intro;
	}


	@Column(length=100)
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}


	public int getLanguage() {
		return this.language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}


	public int getLastusedate() {
		return this.lastusedate;
	}

	public void setLastusedate(int lastusedate) {
		this.lastusedate = lastusedate;
	}


	public int getMarkin() {
		return this.markin;
	}

	public void setMarkin(int markin) {
		this.markin = markin;
	}


	public int getMarkout() {
		return this.markout;
	}

	public void setMarkout(int markout) {
		this.markout = markout;
	}


	public int getMaxlevel() {
		return this.maxlevel;
	}

	public void setMaxlevel(int maxlevel) {
		this.maxlevel = maxlevel;
	}


	public int getMediumcode() {
		return this.mediumcode;
	}

	public void setMediumcode(int mediumcode) {
		this.mediumcode = mediumcode;
	}


	@Column(length=40)
	public String getMediumname() {
		return this.mediumname;
	}

	public void setMediumname(String mediumname) {
		this.mediumname = mediumname;
	}


	public int getMediumtype() {
		return this.mediumtype;
	}

	public void setMediumtype(int mediumtype) {
		this.mediumtype = mediumtype;
	}


	public int getMood() {
		return this.mood;
	}

	public void setMood(int mood) {
		this.mood = mood;
	}


	@Column(length=40)
	public String getMotive() {
		return this.motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}


	public int getMusicformat() {
		return this.musicformat;
	}

	public void setMusicformat(int musicformat) {
		this.musicformat = musicformat;
	}


	@Column(length=20)
	public String getMusicid() {
		return this.musicid;
	}

	public void setMusicid(String musicid) {
		this.musicid = musicid;
	}


	@Column(length=40)
	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public int getOutro() {
		return this.outro;
	}

	public void setOutro(int outro) {
		this.outro = outro;
	}


	@Column(length=10)
	public String getPersonal() {
		return this.personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}


	public int getPlaytime() {
		return this.playtime;
	}

	public void setPlaytime(int playtime) {
		this.playtime = playtime;
	}


	@Column(length=10)
	public String getPool() {
		return this.pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}


	public int getPresenter() {
		return this.presenter;
	}

	public void setPresenter(int presenter) {
		this.presenter = presenter;
	}


	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}


	public int getProduct() {
		return this.product;
	}

	public void setProduct(int product) {
		this.product = product;
	}


	public int getProductgroup() {
		return this.productgroup;
	}

	public void setProductgroup(int productgroup) {
		this.productgroup = productgroup;
	}


	public int getProduction() {
		return this.production;
	}

	public void setProduction(int production) {
		this.production = production;
	}


	public int getProgram() {
		return this.program;
	}

	public void setProgram(int program) {
		this.program = program;
	}


	public int getProject() {
		return this.project;
	}

	public void setProject(int project) {
		this.project = project;
	}


	public short getRamp() {
		return this.ramp;
	}

	public void setRamp(short ramp) {
		this.ramp = ramp;
	}


	public int getRecorddate() {
		return this.recorddate;
	}

	public void setRecorddate(int recorddate) {
		this.recorddate = recorddate;
	}


	@Column(length=40)
	public String getRegistration() {
		return this.registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}


	@Lob
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public short getReplflags() {
		return this.replflags;
	}

	public void setReplflags(short replflags) {
		this.replflags = replflags;
	}


	@Column(length=40)
	public String getReplident() {
		return this.replident;
	}

	public void setReplident(String replident) {
		this.replident = replident;
	}


	public short getRessort() {
		return this.ressort;
	}

	public void setRessort(short ressort) {
		this.ressort = ressort;
	}


	public int getSamplerate() {
		return this.samplerate;
	}

	public void setSamplerate(int samplerate) {
		this.samplerate = samplerate;
	}


	public int getSeasonal() {
		return this.seasonal;
	}

	public void setSeasonal(int seasonal) {
		this.seasonal = seasonal;
	}


	public int getSendrights() {
		return this.sendrights;
	}

	public void setSendrights(int sendrights) {
		this.sendrights = sendrights;
	}


	public short getSoftdel() {
		return this.softdel;
	}

	public void setSoftdel(short softdel) {
		this.softdel = softdel;
	}


	@Column(length=40)
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}


	public int getSpeaker() {
		return this.speaker;
	}

	public void setSpeaker(int speaker) {
		this.speaker = speaker;
	}


	public int getSpotlength() {
		return this.spotlength;
	}

	public void setSpotlength(int spotlength) {
		this.spotlength = spotlength;
	}


	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}


	public int getStory() {
		return this.story;
	}

	public void setStory(int story) {
		this.story = story;
	}


	public int getStyle() {
		return this.style;
	}

	public void setStyle(int style) {
		this.style = style;
	}


	public short getSubressort() {
		return this.subressort;
	}

	public void setSubressort(short subressort) {
		this.subressort = subressort;
	}


	public short getTag() {
		return this.tag;
	}

	public void setTag(short tag) {
		this.tag = tag;
	}


	public int getTarget() {
		return this.target;
	}

	public void setTarget(int target) {
		this.target = target;
	}


	public int getTempo() {
		return this.tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}


	@Column(length=80)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public int getUsecount() {
		return this.usecount;
	}

	public void setUsecount(int usecount) {
		this.usecount = usecount;
	}


	public short getWeekday() {
		return this.weekday;
	}

	public void setWeekday(short weekday) {
		this.weekday = weekday;
	}

}