package mops.portfolios.demodata;


import com.github.javafaker.Faker;

import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.usergroup.Group;
import mops.portfolios.domain.usergroup.User;


import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DemoDataGenerator {

    private transient final Faker faker = new Faker(Locale.GERMAN);

    private EntryField generateEntryField(Entry entry) {
        EntryField entryField = new EntryField();
        entryField.setAttachment(faker.shakespeare().hamletQuote());
        entryField.setContent(faker.shakespeare().kingRichardIIIQuote());
        entryField.setTitle(faker.shakespeare().romeoAndJulietQuote());
        return entryField;
    }

    private List<EntryField> generateEntryFieldList(Entry entry) {
        return IntStream.range(0, 3).mapToObj(
                value -> generateEntryField(entry)).collect(Collectors.toList());
    }

    private Entry generateGroupEntry(){
        Entry entry = new Entry();
        entry.setTitle(faker.shakespeare().romeoAndJulietQuote());
        entry.getFields().addAll(generateEntryFieldList(entry));
        return entry;
    }

    private List<Entry> generateGroupEntryList() {
        return IntStream.range(0, 3).mapToObj(
                value -> generateGroupEntry()).collect(Collectors.toList());
    }

    public Portfolio generateGroupPortfolio() {
        Portfolio portfolio = new Portfolio(faker.shakespeare().romeoAndJulietQuote(),generateGroup());
        portfolio.getEntries().addAll(generateGroupEntryList());
        return portfolio;
    }


    private Entry generateUserEntry() {
        Entry entry = new Entry();
        entry.setTitle(faker.shakespeare().romeoAndJulietQuote());
        entry.setFields(generateEntryFieldList(entry));
        return entry;
    }

    private List<Entry> generateUserEntryList() {
        return IntStream.range(0, 3).mapToObj(
                value -> generateUserEntry()).collect(Collectors.toList());
    }

    public Portfolio generateUserPortfolio() {
        Portfolio portfolio = new Portfolio(faker.shakespeare().romeoAndJulietQuote(), generateUser());
        portfolio.getEntries().addAll(generateUserEntryList());
        return portfolio;
    }

    private User generateUser() {
        HashSet<String> roles = new HashSet<>();
        roles.add("role_1");
        roles.add("role_2");
        roles.add("role_3");
        return new User(
                "user_name",
                "user_mail",
                "user_image",
                roles,
                "userId");
    }

    private Group generateGroup() {
        return new Group(9876543210L, "test");
    }
}
