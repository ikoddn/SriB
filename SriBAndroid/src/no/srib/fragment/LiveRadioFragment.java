package no.srib.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import no.srib.R;
import no.srib.sribapp.util.GetUrlTask;
import no.srib.sribapp.util.RequestTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class LiveRadioFragment extends Fragment {
	private Spanned spannedValue;
	static final String mpObject = "mpObject";
	static MediaPlayer mp = null;

	public LiveRadioFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.liveradio_fragment, container,
				false);
	
		RequestTask rq = new RequestTask(this);

		rq.execute("http://10.0.2.2:8080/SriBServer/rest/podcast");

		if (mp == null || !mp.isPlaying()) {
			GetUrlTask gu = new GetUrlTask(this);

			gu.execute("http://10.0.2.2:8080/SriBServer/rest/radiourl");
		}else{
			Log.i("Debug","Mp speler eller er ikkje null");
		}
		// GetNewsTask news = new GetNewsTask(this);

		// news.execute();

		return rootView;
	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onResume() {
		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.start();
			}
		}
		super.onResume();
	}

	@Override
	public void onStop() {
		Log.i("Onstop","onstop vart kjørt");
		super.onStop();

	}
	
	
	@Override
	public void onDestroy() {
		Log.i("onDestroy","onDestroy vart kjørt");
		  Bundle savedState = new Bundle();
		    onSaveInstanceState(savedState);
		    Intent data = new Intent();
		    data.putExtra("savedState", savedState);
		   
		super.onDestroy();
	}

	public void startMediaPlayer(final String result)
			throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {
		Log.i("Mediaplayer", "Started");

		if (mp == null) {
			mp = new MediaPlayer();
		}
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

		Uri url = null;

		JSONObject podO = null;

		try {

			podO = new JSONObject(result);
			url = Uri.parse(podO.getString("url"));

		} catch (JSONException e) {
			e.printStackTrace();

		}

		mp.setDataSource(getActivity().getApplicationContext(), url);
		mp.prepareAsync();
		mp.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();

			}
		});

	}

	public void populateTextView(String result) {
		Activity ac = getActivity();
		TextView t2 = (TextView) ac.findViewById(R.id.textView2);
		TextView t1 = (TextView) ac.findViewById(R.id.textView1);
		TextView t3 = (TextView) ac.findViewById(R.id.textView3);
		TextView t4 = (TextView) ac.findViewById(R.id.textView4);
		List<TextView> t = new ArrayList<TextView>();
		t.add(t1);
		t.add(t2);
		t.add(t3);
		t.add(t4);

		JSONArray reader = null;
		JSONObject podO = null;

		try {
			reader = new JSONArray(result);
			podO = reader.getJSONObject(0);
			t.get(0).setText(podO.getString("title"));
			t.get(1).setText(String.valueOf(podO.getInt("createdate")));
			int dur = podO.getInt("duration");
			dur = dur /= 60000;
			t.get(2).setText(String.valueOf(dur) + " min");
			t.get(3).setText(podO.getString("remark"));
		} catch (JSONException e) {
			e.printStackTrace();

		}
	}

	public void addContentToNewsView(ImageGetter result) {

		TextView text = (TextView) getActivity().findViewById(R.id.textView5);

		if (text != null) {

			String html = "<p>Mine favorittrilogier i usortert rekkefølge er følgende: <em>Mad Max</em> (1979, 1981, 1985), <em>Star Wars </em>(1977, 1980, 1983), <em>Indiana Jones </em>(1981, 1984, 1989), <em>Terminator</em> (1985, 1991, 2005), <em>Die Hard</em> (1988, 1990, 1995), <em>Alien </em>(1979, 1986, 1992), <em>Evil Dead</em> (1981, 1987, 1992), <em>Back to the Future</em> (1985, 1989, 1990). Som dere ser er mange av trilogiene ovenfor innom 80-tallet, selv om de fleste strekker seg utover flere tiår. <em>Star Wars</em> er selvfølgelig den beste, og den beste i trilgoien, <em>The Empire Strikes Back</em> kom i 1980.</p>\n<p align=\"LEFT\"><strong>LES OGSÅ:</strong> <a href=\"http://srib.no/2014/02/20/kinosyndromets-definitive-guide-til-80-tallet-del-1/\">Kinosyndromets definitive guide til 80-tallet &#8211; DEL 1</a></p>\n<p align=\"LEFT\">Nå skal jeg prøve ikke å gjøre dette til en artikkel med de samme argumentene som idiotgjøkfolk som meg sier, men jeg føler likevel at jeg skylder dere en liten forklaring på hvorfor jeg er så bastant. Kort oppsummert: Han Solo er verdens tøffeste fyr, Vader er verdens tøffeste slemming, Millennium Falcon er verdens tøffeste romskip, og det meste i filmen er utrolig deilig å se på. Kanskje utenom rumpefjeset til en gangster i baren på Mos Isley (hva fanken?)</p>\n<div id=\"attachment_36266\" style=\"width: 760px\" class=\"wp-caption aligncenter\"><a href=\"http://srib.no/wp-content/uploads/2014/03/rumpefjes.png\"><figure class=\"hammy-responsive size-large wp-image-36266    \" title=\"\"  data-media=\"http://srib.no/wp-content/uploads/cache/2014/03/rumpefjes-750x448/3072893820.png\" data-media240=\"http://srib.no/wp-content/uploads/cache/2014/03/rumpefjes-750x448/2400403710.png\" data-media320=\"http://srib.no/wp-content/uploads/cache/2014/03/rumpefjes-750x448/1240689470.png\" data-media480=\"http://srib.no/wp-content/uploads/cache/2014/03/rumpefjes-750x448/2247312060.png\"><noscript><img src=\"http://srib.no/wp-content/uploads/2014/03/rumpefjes-750x448.png\" alt=\"Foto: skjermdump YouTube\" title=\"\" width=\"750\" height=\"448\"></noscript></figure></a><p class=\"wp-caption-text\">Rumpefjesgangster koser seg på bar på Mos Isley. (Foto: Skjermdump/YouTube)</p></div>\n<h3>TRILOGIENS TRILOGI</h3>\n<p>Hvis jeg <em>må </em>velge tre trilogier, en trilogienes trilogi om du vil, velger jeg <em>Star Wars</em><strong>, </strong><em>Indiana Jones</em><strong> </strong>og <em>Tilbake til fremtiden</em><strong>. </strong>Jeg orker ikke å skrive om alle trilogiene jeg nevnte innledningsvis, så da får det bli med de tre. I tillegg tenkte jeg å trekke frem noen enkeltfilmer som har fått kultstatus, siden disse trilogiene på mange måter også går under den kategorien.</p>\n<h3>INDIANA JONES</h3>\n<p>Jaja. Nok om Star Wars. Men ikke nok om Lucas! For han skrev og produserte <em>Raiders of the Lost Ark</em>, som heldigvis var regissert av Steven Spielberg. For Indiana Jones-trilogien kom i sin helhet på 80-tallet, med <em>Raiders</em> i 81, <em>Temple of Doom</em> i 84 og <em>The Last Crusade</em> i 89. Jeg trenger vel ikke å si noe særlig om hvorfor den fortjener en plass i listen over de beste trilogiene i filmhistorien, for Indiana Jones, spilt av Han Solo(!), er uten tvil filmhistoriens tøffeste skattejeger. <em>Temple of Doom</em> er den svakeste av de tre, men er verdt å se selv om Short Round har fått rollen som Jar Jar Binks. Jeg tror forøvrig dette er et bra tidspunkt å tipse om <a href=\"http://www.imdb.com/title/tt1325014/?ref_=fn_al_tt_5\">The People vs. George Lucas</a>, en dokumentar som er mer eller mindre akkurat det tittelen sier, som er tilgjengelig på Netflix. Det er kanskje også verdt påpeke at <em>Raiders</em> har en av de mest perfekte introene i filmhistorien;</p>\n<span class='embed-youtube' style='text-align:center; display: block;'><iframe class='youtube-player' type='text/html' width='560' height='315' src='http://www.youtube.com/embed/Pr-8AP0To4k?version=3&#038;rel=1&#038;fs=1&#038;showsearch=0&#038;showinfo=1&#038;iv_load_policy=1&#038;wmode=transparent' frameborder='0'></iframe></span>\n<h3></h3>\n<h3>TILBAKE TIL FREMTIDEN</h3>\n<p><em>Tilbake til fremtiden</em> er 80-tallet i filmformat. Den inneholder alt som var spesielt med filmer på 80-tallet. Hovedrollen Marty McFly minner litt om <a href=\"http://srib.no/2014/02/20/kinosyndromets-definitive-guide-til-80-tallet-del-1/\">Ferris Buellers rolle som Ace</a>, selv om han ikke har like full kontroll på</p>\n<p>verden, og han klarer alltid, på en litt vimsete og uelegant måte å takle en hvilken som helst situasjon med suksess. Mye av filmen som plottet, karakterene, små detaljer som pepsicaféen i <em>Tilbake til fremtiden II, </em>har en slags likegyldig og fornøyd holdning til at ting er tullete og lettbeint. Dette finner jeg ikke noe særlig i filmer fra andre tiår. Dessuten har Huey Lewis and the News den tonesettende åpningslåta, etter verdens mest bekymringsløst teite intro med Marty som spiller gitar med en forsterker på størrelse med en studenthybel.</p>\n<p>En annen ting jeg liker godt ved åpningsscenen er fascinasjonen for mekaniske dingser som gjør lettbeinte ting, som en også stort sett kun finner i filmer fra 80-tallet (for eksempel boksehandskebeltet til Data i <em>The Goonies</em>). I <em>Tilbake til fremtiden II </em>drar Marty til fremtiden, nærmere bestemt 16:29 21 Oktober 2015, og det viser seg at fremtiden er litt som 80-tallet på speed. Hver eneste lille del av fremtiden, er ekstremt preget av 80-tallet. Det viktigste for meg er likevel den lettbeinte tonen og humoren koblet med en sympatisk, uelegant hovedkarakter og en forferdelig tidsfloke. Jeg liker også at hele filmen er<a href=\"http://www.denofgeek.com/movies/16532/the-plot-holes-and-paradoxes-of-the-back-to-the-future-trilogy\"> et digert tidsparadoks</a> uten at noen i produksjonen ser ut til å ha brydd seg.</p>\n<div id=\"attachment_36656\" style=\"width: 910px\" class=\"wp-caption aligncenter\"><a href=\"http://srib.no/wp-content/uploads/2014/03/4758387047_92ae81bc99_o.jpg\"><figure class=\"hammy-responsive size-full wp-image-36656 \" title=\"\"  data-media=\"http://srib.no/wp-content/uploads/cache/2014/03/4758387047_92ae81bc99_o/667145765.jpg\" data-media240=\"http://srib.no/wp-content/uploads/cache/2014/03/4758387047_92ae81bc99_o/379393393.jpg\" data-media320=\"http://srib.no/wp-content/uploads/cache/2014/03/4758387047_92ae81bc99_o/4063870490.jpg\" data-media480=\"http://srib.no/wp-content/uploads/cache/2014/03/4758387047_92ae81bc99_o/4112853400.jpg\"><noscript><img src=\"http://srib.no/wp-content/uploads/2014/03/4758387047_92ae81bc99_o.jpg\" alt=\"4758387047_92ae81bc99_o\" title=\"\" width=\"900\" height=\"601\"></noscript></figure></a><p class=\"wp-caption-text\">Klokken i &#8220;Tilbake til fremtiden&#8221;. (Foto: gnews pics/Flickr)</p></div>\n<h3><span style=\"font-size: 1.17em; line-height: 1.5em;\">ANDRE FILMER MED KULTOPPSLUTNING</span></h3>\n<p><strong>Willow (1988)<br />\n</strong>Dette er en film jeg tror George Lucas skrev fordi han har et litt usunt kjært forhold til de korteste av oss. For <em>Willow</em>, i likhet med <em>Jabba the Hut</em>, er stappfull av dverger. Hoveddvergen, Willow,  er spilt av Warwick Davis, som vi for et par år kunne se med Karl Pilkington i<em> Idiot Abroad</em>, og i <em>Life&#8217;s too short</em> med Ricky Gervais. Han spilte forøvrig ewoken Wicket i <em>Return of the Jedi</em>.</p>\n<div id=\"attachment_36430\" style=\"width: 760px\" class=\"wp-caption aligncenter\"><a href=\"http://srib.no/wp-content/uploads/2014/03/4231.png\"><figure class=\"hammy-responsive size-large wp-image-36430  \" title=\"\"  data-media=\"http://srib.no/wp-content/uploads/cache/2014/03/4231-750x447/1474428610.png\" data-media240=\"http://srib.no/wp-content/uploads/cache/2014/03/4231-750x447/3690565043.png\" data-media320=\"http://srib.no/wp-content/uploads/cache/2014/03/4231-750x447/4089494718.png\" data-media480=\"http://srib.no/wp-content/uploads/cache/2014/03/4231-750x447/102985804.png\"><noscript><img src=\"http://srib.no/wp-content/uploads/2014/03/4231-750x447.png\" alt=\"Foto: YouTube\" title=\"\" width=\"750\" height=\"447\"></noscript></figure></a><p class=\"wp-caption-text\">Oscar-øyeblikk for Warwick Davis i Willow. (Foto: Skjermdump/YouTube)</p></div>\n<p><strong>Ladyhawke (1985)<br />\n</strong>Det hadde vært ganske så døvt å hevde at <em>Ladyhawke</em> er en god film på ekte, men vi kan jo late som da? Den er jo ganske hyggelig å se på, i hvert fall for folk som setter pris på den eightiesianske bekymringsløsheten. Filmen handler om at Matthew Broderick rømmer fra et fangehull i en middelaldersk setting. Så møter han en fyr som blir til en ulv og en sexy, sexy 80&#8242;s chick som blir til en hauk, eller en ladyhauk om du vil.</p>\n<p><strong>Highlander (1986)<br />\n</strong><em>Highlander</em> er litt sånn Peter Pan for voksne. Protagonisten lever for alltid, med mindre noen kutter av han hodet. Her er det igjen, ganske tåpelig premiss, spesielt når det virker som å være et seriøst forsøk på en voksenfilm, men at produsentene bare godtok det bekymringsløst. Sean Connery har en minneverdig tåpelig birolle som læremesteren Juan Sanchez Villa-Lobos Ramirez. Ikke den teiteste rollen i hans karriere, kun slått av den dummeste filmen i universet, <em>Zardos</em>.</p>\n<p><strong><span class='embed-youtube' style='text-align:center; display: block;'><iframe class='youtube-player' type='text/html' width='560' height='315' src='http://www.youtube.com/embed/cMRgv11fIjA?version=3&#038;rel=1&#038;fs=1&#038;showsearch=0&#038;showinfo=1&#038;iv_load_policy=1&#038;wmode=transparent' frameborder='0'></iframe></span></strong></p>\n<p><strong>The Goonies (1985)<br />\n</strong><em>The Goonies</em> handler om en gjeng med smarte unger (en av de spilt av Sean Astin), og en tjukkas for comic relief, som finner et gammelt sjørøverkart, og dermed drar på jakt etter en forsvunnet skatt. Mest fornøyelig med hele filmen er rollen til Jonathan Ke Quan, som spiller tidligere nevnte Short Round i <em>Temple of Doom</em>. Rollen hans som Data er det mekaniske geniet i gjengen, og som tidligere nevnt har han et boksehandskebelte som han banker skurker med på kosteligste vis.</p>\n<h3><strong>KOMMER I DEL 3</strong></h3>\n<p>I del 3 skal jeg skrive om komedier, Eddie Murphy, og den eightiesianske bekymringsløsheten ovenfor hudfarge. Hjelpes, det var en lykkelig tid.</p>\n";
			spannedValue = Html.fromHtml(html, result, null);
			text.setText(spannedValue);
		} else {
			Log.i("Debug", "Denne er null");
		}

	}

}
