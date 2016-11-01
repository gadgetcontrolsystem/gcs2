package kz.gcs.data.dummy;

import kz.gcs.domain.DashboardNotification;
import kz.gcs.maps.client.LatLon;

import java.io.Serializable;
import java.util.*;

public abstract class DummyDataGenerator implements Serializable{

    private static final long serialVersionUID = 679449350509204698L;

    static Map<String, LatLon> locations =new HashMap<String, LatLon>() {
        private static final long serialVersionUID = -5258940643725058078L;

        {
            put("Алматы", new LatLon(43.238949, 76.889709));
            put("Астана", new LatLon(51.144695, 71.478844));
            put("Караганда", new LatLon(49.8047, 73.1094));
            put("Чимкент", new LatLon(42.3417, 69.5901));
        }
    };

    static LatLon randomCoordinate(String city) {
        return DummyDataGenerator.locations.get(city);
    }

    static String randomCity() {
        String[] cities = {"Алматы", "Астана", "Караганда", "Чимкент"};
        return cities[(int) (Math.random() * cities.length)];
    }

    static String randomFirstName() {
        String[] names = { "Абзал", "Кайдар", "Бауыржан", "Хархабат"};
        return names[(int) (Math.random() * names.length)];
    }

    static String randomLastName() {
        String[] names = { "Айтенов", "Жунусов", "Бейсекеев", "Темирбаев"};
        return names[(int) (Math.random() * names.length)];
    }

    static String randomCompanyName() {

        String name = randomName();
        if (Math.random() < 0.03) {
            name += " Technologies";
        } else if (Math.random() < 0.02) {
            name += " Investment";
        }
        if (Math.random() < 0.3) {
            name += " Inc";
        } else if (Math.random() < 0.2) {
            name += " Ltd.";
        }

        return name;
    }

    public static String randomWord(int len, boolean capitalized) {
        String[] part = { "ger", "ma", "isa", "app", "le", "ni", "ke", "mic",
                "ro", "soft", "wa", "re", "lo", "gi", "is", "acc", "el", "tes",
                "la", "ko", "ni", "ka", "so", "ny", "mi", "nol", "ta", "pa",
                "na", "so", "nic", "sa", "les", "for", "ce" };
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            String p = part[(int) (Math.random() * part.length)];
            if (i == 0 && capitalized) {
                p = Character.toUpperCase(p.charAt(0)) + p.substring(1);
            }
            sb.append(p);
        }
        return sb.toString();

    }

    public static String randomText(int words) {
        StringBuffer sb = new StringBuffer();
        int sentenceWordsLeft = 0;
        while (words-- > 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            if (sentenceWordsLeft == 0 && words > 0) {
                sentenceWordsLeft = (int) (Math.random() * 15);
                sb.append(randomWord(1 + (int) (Math.random() * 3), true));
            } else {
                sentenceWordsLeft--;
                sb.append(randomWord(1 + (int) (Math.random() * 3), false));
                if (words > 0 && sentenceWordsLeft > 2 && Math.random() < 0.2) {
                    sb.append(',');
                } else if (sentenceWordsLeft == 0 || words == 0) {
                    sb.append('.');
                }
            }
        }
        return sb.toString();
    }

    static String randomName() {
        int len = (int) (Math.random() * 4) + 1;
        return randomWord(len, true);
    }

    static String randomTitle(int words) {
        StringBuffer sb = new StringBuffer();
        int len = (int) (Math.random() * 4) + 1;
        sb.append(randomWord(len, true));
        while (--words > 0) {
            len = (int) (Math.random() * 4) + 1;
            sb.append(' ');
            sb.append(randomWord(len, false));
        }
        return sb.toString();
    }

    static String randomHTML(int words) {
        StringBuffer sb = new StringBuffer();
        while (words > 0) {
            sb.append("<h2>");
            int len = (int) (Math.random() * 4) + 1;
            sb.append(randomTitle(len));
            sb.append("</h2>");
            words -= len;
            int paragraphs = 1 + (int) (Math.random() * 3);
            while (paragraphs-- > 0 && words > 0) {
                sb.append("<p>");
                len = (int) (Math.random() * 40) + 3;
                sb.append(randomText(len));
                sb.append("</p>");
                words -= len;
            }
        }
        return sb.toString();
    }

    static Collection<DashboardNotification> randomNotifications() {
        DashboardNotification n1 = new DashboardNotification();
        n1.setId(1);
        n1.setFirstName(randomFirstName());
        n1.setLastName(randomLastName());
        n1.setAction("created a new report");
        n1.setPrettyTime("25 minutes ago");
        n1.setContent(randomText(18));

        DashboardNotification n2 = new DashboardNotification();
        n2.setId(2);
        n2.setFirstName(randomFirstName());
        n2.setLastName(randomLastName());
        n2.setAction("changed the schedule");
        n2.setPrettyTime("2 days ago");
        n2.setContent(randomText(10));

        return Arrays.asList(n1, n2);
    }

    public static int[] randomSparklineValues(int howMany, int min, int max) {
        int[] values = new int[howMany];

        for (int i = 0; i < howMany; i++) {
            values[i] = (int) (min + (Math.random() * (max - min)));
        }

        return values;
    }

}