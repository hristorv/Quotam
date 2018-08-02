package com.quotam.adapters;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.quotam.R;
import com.quotam.model.Album;
import com.quotam.model.Artist;
import com.quotam.model.Comment;
import com.quotam.model.Font;
import com.quotam.model.ImageItem;
import com.quotam.model.Image;
import com.quotam.model.ImageData;
import com.quotam.model.ImageDefault;
import com.quotam.model.SectionFooter;
import com.quotam.model.SectionHeader;
import com.quotam.model.Section;
import com.quotam.model.TextQuote;

public class Items {


    public static final int MAX_ITEMS_VIEW = 8;
    private int[] pics = {R.drawable.cat_wisdom,
            R.drawable.test_albums1, R.drawable.test_albums2, R.drawable.test_albums3, R
            .drawable.test_albums4, R.drawable.test_albums5, R.drawable.test_albums6, R
            .drawable.test_albums7, R.drawable.test_albums8, R.drawable.test_albums9, R
            .drawable.test_albums10, R.drawable.test_albums11, R.drawable.test_albums12, R
            .drawable.test_albums13, R.drawable.test_albums14, R.drawable.test_albums15, R
            .drawable.test_albums16, R.drawable.test_albums17, R.drawable.test_albums18, R.drawable.cat_favourites, R.drawable.cat_funny, R.drawable
            .cat_happiness, R.drawable.cat_inspirational, R.drawable.cat_life, R.drawable
            .cat_love, R.drawable.cat_success, R.drawable.cat_travel,R.drawable.ocr1,R.drawable.ocr2,R.drawable.ocr3,R.drawable.ocr4,R.drawable.ocr5};
    private String[] albumNames = {
            "Gentlements guide", "Celebrities", "Entrepenuer", "Game of thrones", "MyAlbum", "Breaking bad", "RandomAlbum", "Loveee"
    };
    private String[] artistNames = {
            "Ivan Georgiev", "hristo_r", "gosho95", "Donald Trump", "randomName", "Petar88", "Mariq_Georgieva", "john_snow", "MMMMMMMMMMMMMMMMMMMM"
    };

    private String randomAlbumName() {
        int rnd = new Random().nextInt(albumNames.length);
        return albumNames[rnd];
    }

    private String randomArtist() {
        int rnd = new Random().nextInt(artistNames.length);
        return artistNames[rnd];
    }

    private String randomPicUrl() {
        int rnd = new Random().nextInt(pics.length);
        return "drawable://" + pics[rnd];
    }

    private String randomNum() {
        return String.valueOf(new Random().nextInt(100));
    }


    public Image getImage() {
        return new Image(randomPicUrl(), randomNum(), randomArtist());
    }

    public Album getAlbum() {
        return new Album(randomAlbumName(), randomPicUrl(), randomNum(), randomNum());
    }

    public Artist getArtist() {
        return new Artist(randomArtist(), randomPicUrl(), randomPicUrl(), randomNum(), randomNum(), randomNum());
    }

    public List<Object> getRandomItems(int types) {

        ArrayList<Object> items = new ArrayList<Object>();
        for (int i = 0; i < 100; i++) {
            Object object = null;
            int num = randomNumSize(27);
            switch (new Random().nextInt(types)) {
                case 0:
                    object = new Image("drawable://" + pics[num], randomNum(), randomArtist());
                    break;
                case 1:
                    object = new Album(randomAlbumName(), "drawable://" + pics[num], randomNum(), randomNum());
                    break;
                case 2:
                    object = new Artist(randomArtist(), "drawable://" + pics[num], "drawable://" + pics[26 - i], randomNum(), randomNum(), randomNum());
                    break;
                default:
                    object = new Image("drawable://" + pics[26 - num], randomNum(), randomArtist());
                    break;
            }
            if (object != null)
                items.add(object);
        }
        return items;
    }

    public List<Object> getRandomPictures() {
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Object object = new Image(randomPicUrl(), randomNum(), randomArtist());
            items.add(object);
        }
        Collections.shuffle(items);
        return items;
    }

    public List<Object> getRandomPicturesRandomSize() {
        List<Object> items = new ArrayList<>();
        int size = new Random().nextInt(20);
        for (int i = 0; i < size; i++) {
            Object object = new Image(randomPicUrl(), randomNum(), randomArtist());
            items.add(object);
        }
        Collections.shuffle(items);
        return items;
    }


    public List<Object> getRandomAlbums(int count) {
        ArrayList<Object> items = new ArrayList<Object>();
        for (int i = 0; i < count; i++) {
            Object object = new Album(randomAlbumName(), "drawable://" + pics[randomNumSize(27)], randomNum(), randomNum());
            items.add(object);
        }
        Collections.shuffle(items);
        return items;
    }

    private int randomNumSize(int i) {
        return new Random().nextInt(i);
    }

    public List<Object> getRandomArtists() {
        ArrayList<Object> items = new ArrayList<Object>();
        for (int i = 0; i < 100; i++) {
            items.add(getArtist());
        }
        Collections.shuffle(items);
        return items;
    }


    public List<Object> getCategories() {

        return new ArrayList<Object>(Arrays.asList(ImageData.getInstance().getCategories()));


    }

    public List<Object> getRandomComments() {
        ArrayList<Object> items = new ArrayList<Object>();
        for (int i = 0; i < 20; i++) {
            Object object = new Comment("drawable://" + pics[i], randomArtist(), "This is awesome comment for an awesome album or picture, by an awesome artist. Pretty awesome stuff.");
            items.add(object);
        }
        Collections.shuffle(items);
        return items;

    }

    private ArrayList<Object> getRandomSection() {
        ArrayList<Object> section = new ArrayList<Object>();
        section.addAll(getRandomPicturesRandomSize());
        SectionHeader timelineHeader = new SectionHeader(randomAlbumName(), "Newest", "2d");
        boolean isActive = false;
        String count = "0";
        if (section.size() > MAX_ITEMS_VIEW) {
            isActive = true;
            count = String.valueOf(section.size() - MAX_ITEMS_VIEW);
        }
        SectionFooter timelineFooter = new SectionFooter(isActive, count);
        section.add(0, timelineHeader);
        section.add(timelineFooter);
        return section;
    }

    public List<Object> getSections() {
        ArrayList<Object> sections = new ArrayList<Object>();
        for (int i = 0; i < 20; i++) {
            sections.addAll(getRandomSection());
        }
        return sections;
    }

    public List<Section> getRandomSectionObjectList() {
        ArrayList<Section> sections = new ArrayList<Section>();
        for (int i = 0; i < 100; i++) {
            Section section = getRandomSectionObject();
            if (section.items.size() > 0)
                sections.add(section);
        }
        return sections;
    }

    private Section getRandomSectionObject() {
        Section section = new Section();
        section.items = (ArrayList<Object>) getRandomPicturesRandomSize();
        section.header = new SectionHeader(randomAlbumName(), "Newest", "2d");
        boolean isActive = false;
        String count = "0";
        if (section.items.size() > MAX_ITEMS_VIEW) {
            isActive = true;
            count = String.valueOf(section.items.size() - MAX_ITEMS_VIEW);
        }
        section.footer = new SectionFooter(isActive, count);

        return section;
    }

    public List<Section> getSchedulerItemsSections() {
        ArrayList<Section> sections = new ArrayList<Section>();
        // Categories
        Section categorySection = new Section();
        categorySection.items = (ArrayList<Object>) getCategories();
        categorySection.header = new SectionHeader("Categories", "");
        categorySection.footer = new SectionFooter();
        sections.add(categorySection);
        // Albums collection
        Section albumsCollectionSection = new Section();
        albumsCollectionSection.items = (ArrayList<Object>) getRandomAlbums(5);
        albumsCollectionSection.header = new SectionHeader("Albums", "Collection");
        albumsCollectionSection.footer = new SectionFooter();
        sections.add(albumsCollectionSection);
        // Albums favorites
        Section albumsFavoritesSection = new Section();
        albumsFavoritesSection.items = (ArrayList<Object>) getRandomAlbums(10);
        albumsFavoritesSection.header = new SectionHeader("Albums", "Favorites");
        albumsFavoritesSection.footer = new SectionFooter();
        sections.add(albumsFavoritesSection);

        return sections;
    }

//    public List<Scheduler> getRandomSchedulers() {
//        ArrayList<Scheduler> schedulers = new ArrayList<Scheduler>();
//        schedulers.add(new Scheduler(Scheduler.Type.Wallpaper, "1h", getRandomAlbums(2)));
//        schedulers.add(new Scheduler(Scheduler.Type.Wallpaper, "15m", getRandomAlbums(1)));
//        schedulers.add(new Scheduler(Scheduler.Type.Notification, "1d", getRandomAlbums(3)));
//        schedulers.add(new Scheduler(Scheduler.Type.Widget, "12h", getRandomAlbums(5)));
//        return schedulers;
//    }

    public List<TextQuote> getTextQuotes() {
        ArrayList<TextQuote> textQuotes = new ArrayList<>();
        textQuotes.add(new TextQuote("Don't cry because it's over, smile because it happened.", "Dr. Seuss"));
        textQuotes.add(new TextQuote("I'm selfish, impatient and a little insecure. I make mistakes, I am out of control and at times hard to handle. But if you can't handle me at my worst, then you sure as hell don't deserve me at my best.", "Marilyn Monroe"));
        textQuotes.add(new TextQuote("Be yourself; everyone else is already taken.", "Oscar Wilde"));
        textQuotes.add(new TextQuote("Two things are infinite: the universe and human stupidity; and I'm not sure about the universe.", "Albert Einstein"));
        textQuotes.add(new TextQuote("So many books, so little time.", "Frank Zappa"));
        textQuotes.add(new TextQuote("Be who you are and say what you feel, because those who mind don't matter, and those who matter don't mind.", "Bernard M. Baruch"));
        textQuotes.add(new TextQuote("You've gotta dance like there's nobody watching,\n" +
                "Love like you'll never be hurt,\n" +
                "Sing like there's nobody listening,\n" +
                "And live like it's heaven on earth.", "William W. Purkey"));
        textQuotes.add(new TextQuote("A room without books is like a body without a soul.", "Marcus Tullius Cicero"));
        textQuotes.add(new TextQuote("You know you're in love when you can't fall asleep because reality is finally better than your dreams.", "Dr. Seuss"));
        textQuotes.add(new TextQuote("You only live once, but if you do it right, once is enough.", "Mae West"));
        textQuotes.add(new TextQuote("Be the change that you wish to see in the world.", "Mahatma Gandhi"));
        textQuotes.add(new TextQuote("In three words I can sum up everything I've learned about life: it goes on.", "Robert Frost"));

        return textQuotes;
    }

    public ImageDefault getImageDefault() {
        return new ImageDefault(randomPicUrl(), ImageDefault.Type.COLLECTION);
    }

    public List<ImageDefault> getRandomImageDefault() {
        List<ImageDefault> items = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            items.add(getImageDefault());
        Collections.shuffle(items);
        return items;
    }

    public List<Font> getFonts() {
        List<Font> fonts = new ArrayList<>();

        fonts.add(new Font("Abel", "drawable://" + R.drawable.abel));
        fonts.add(new Font("Acme", "drawable://" + R.drawable.acme));
        fonts.add(new Font("Actor", "drawable://" + R.drawable.actor));
        fonts.add(new Font("Adamina", "drawable://" + R.drawable.adamina));
        fonts.add(new Font("Akronim", "drawable://" + R.drawable.akronim));
        fonts.add(new Font("Aladin", "drawable://" + R.drawable.aladin));
        fonts.add(new Font("Alef", "drawable://" + R.drawable.alef));
        fonts.add(new Font("Amiko", "drawable://" + R.drawable.amiko));
        fonts.add(new Font("Amiri", "drawable://" + R.drawable.amiri));
        fonts.add(new Font("Amita", "drawable://" + R.drawable.amita));

        return fonts;
    }

    public List<ImageItem> getFrames() {
        List<ImageItem> imageItems = new ArrayList<>();
        addFramesWithCategory(imageItems, "Vintage");
        addFramesWithCategory(imageItems, "Modern");
        addFramesWithCategory(imageItems, "Tech");
        addFramesWithCategory(imageItems, "Old");
        addFramesWithCategory(imageItems, "Art");
        return imageItems;
    }

    private void addFramesWithCategory(List<ImageItem> imageItems, String category) {
        imageItems.add(new ImageItem("file:///android_asset/frames/01.png", "file:///android_asset/frames/01.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/02.png", "file:///android_asset/frames/02.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/03.png", "file:///android_asset/frames/03.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/04.png", "file:///android_asset/frames/04.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/05.png", "file:///android_asset/frames/05.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/06.png", "file:///android_asset/frames/06.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/07.png", "file:///android_asset/frames/07.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/08.png", "file:///android_asset/frames/08.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/09.png", "file:///android_asset/frames/09.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/10.png", "file:///android_asset/frames/10.thumb.jpg", category));
        imageItems.add(new ImageItem("file:///android_asset/frames/11.png", "file:///android_asset/frames/11.thumb.jpg", category));
    }

    List<String> defaultUrls = Arrays.asList("beep_screen.jpg", "bocea_overlay.jpg", "bubble_overlay.jpg", "burn_overlay.jpg", "cyborg_screen.jpg");

    public List<ImageItem> getOverlays() {
        List<ImageItem> imageItems = new ArrayList<>();
        addOverlaysWithCategory(imageItems, defaultUrls, "Default");

        return imageItems;
    }

    private void addOverlaysWithCategory(List<ImageItem> imageItems, List<String> urls, String category) {
        String baseUrl = "file:///android_asset/overlays/";
        String baseUrlThumb = baseUrl + "thumb/";
        for (String url : urls)
            imageItems.add(new ImageItem(baseUrl + url, baseUrlThumb + url, category));

    }

}
