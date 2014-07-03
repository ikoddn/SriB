package no.srib.app.server.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import no.srib.app.server.model.jpa.Definition;

public class DefinitionNameComparator implements Comparator<Definition> {

    private Collator collator;

    public DefinitionNameComparator(final Locale locale) {
        collator = Collator.getInstance(locale);
    }

    @Override
    public int compare(final Definition o1, final Definition o2) {
        int nameCompare = collator.compare(o1.getName(), o2.getName());

        if (nameCompare == 0) {
            return o1.getDefnr() - o2.getDefnr();
        }

        return nameCompare;
    }
}
