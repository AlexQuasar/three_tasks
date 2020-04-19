package com.alexquasar.threeTasks;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class GeneratorUrlUtils {

    private List<String> sites = Arrays.asList(
            "google.com/home", "wiki.org/home" , "youtube.com/home", "yandex.ru/home", "habr.com/home",
            "stackoverflow.com/home", "spring.io/home", "github.com/home", "codeflow.site/home", "codegym.cc/home");

    private Random random = new Random();

    public static void main(String[] args) {
        new GeneratorUrlUtils().generateUrls(1,1,1);
    }
    public List<String> generateUrls(long countUrl, long countDuplicates, long maxDuplicates) {

        List<Set<String>> allUrls = new ArrayList<>();
        Set<String> duplicated = new HashSet<>();

        LocalDateTime start = LocalDateTime.now();
        LinkedList<String> url = new LinkedList<>();

        for (long i = 0; i < 10000000000L; i++) {
            String randomUrl = provideRandomUrl(i);
//            Duration between = Duration.between(now, LocalDateTime.now());
            url.add(randomUrl);
//            if (between.toMillis() > 100){
//                System.out.println("add " + between.toMillis()+" index "+ i);
//            }

            if (i%10_000_000 == 0) {
                System.out.println("point " + i);
                System.out.println(Duration.between(start, LocalDateTime.now()).getSeconds());
                start = LocalDateTime.now();
            }
        }
//        System.out.println("Size: " + set.size());
        System.out.println("Size: " + duplicated.size());

        return null;
    }

    private String provideRandomUrl(long i) {

        if (i >= 100 && i <= 200) {
            return "DuplicateUrl";
        } else if (i >= 300 && i <= 400) {
            return "DuplURL";
        }
        String abcdefghijklm = "abc";
        Character c = abcdefghijklm.charAt(random.nextInt(abcdefghijklm.length()));
        return c.toString() + "randomUrl"+i;
    }

//    }public List<String> generateUrls(long countUrl, long countDuplicates, long maxDuplicates) {
//        List<String> urls = new LinkedList<>();
//
//        long countUrlWithoutDuplicates = countUrl - countDuplicates * maxDuplicates;
//        long stepDuplicates = countUrlWithoutDuplicates / countDuplicates;
//
//        int sizeSites = sites.size();
//        int addDuplicates = 1;
//        for (long i = 0; i < countUrlWithoutDuplicates; i++) {
//
//            int index = random.nextInt(sizeSites);
//            String site = sites.get(index) + i;
//            urls.add(site);
//
//            if (i + 1 == stepDuplicates * addDuplicates && addDuplicates <= countDuplicates) {
//                for (int j = 0; j < maxDuplicates; j++) {
//                    urls.add(site + site);
//                }
//                addDuplicates++;
//            }
//        }
//
//        return urls;
//    }
}
