package com.pes.gkl.gate.Fragments.dummy;

import com.pes.gkl.gate.modelclasses.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Topic> ITEMS = new ArrayList<Topic>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Integer, Topic> ITEM_MAP = new HashMap<Integer, Topic>();

    static {
        // Add 3 sample items.
        addItem(new Topic(1, "Please wait..."));
        //addItem(new Topic("2", "Item 2"));
        //addItem(new Topic("3", "Item 3"));
    }

    private static void addItem(Topic item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    /*
    public static class Topic {
        public String id;
        public String content;

        public Topic(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }*/
}
