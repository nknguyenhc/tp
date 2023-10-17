package networkbook.testutil;

import java.util.List;
import java.util.stream.Collectors;

import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.GraduatingYear;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.tag.Tag;
import networkbook.model.util.SampleDataUtil;
import networkbook.model.util.UniqueList;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_LINK = "github.com/amybeez";
    public static final String DEFAULT_GRADUATING_YEAR = "2000";
    public static final String DEFAULT_COURSE = "Computer Science";
    public static final String DEFAULT_SPECIALISATION = "Game Development";
    public static final String DEFAULT_PRIORITY = null;

    private Name name;
    private Phone phone;
    private UniqueList<Email> emails;
    private UniqueList<Link> links;
    private GraduatingYear graduatingYear;
    private Course course;
    private Specialisation specialisation;
    private UniqueList<Tag> tags;
    private Priority priority;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        emails = new UniqueList<Email>().setItems(List.of(new Email(DEFAULT_EMAIL)));
        links = new UniqueList<Link>().setItems(List.of(new Link(DEFAULT_LINK)));
        graduatingYear = new GraduatingYear(DEFAULT_GRADUATING_YEAR);
        course = new Course(DEFAULT_COURSE);
        specialisation = new Specialisation(DEFAULT_SPECIALISATION);
        tags = new UniqueList<>();
        priority = null;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        emails = personToCopy.getEmails();
        links = personToCopy.getLinks();
        graduatingYear = personToCopy.getGraduatingYear();
        course = personToCopy.getCourse();
        specialisation = personToCopy.getSpecialisation();
        tags = personToCopy.getTags();
        priority = personToCopy.getPriority().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Adds a link to the person we are building.
     */
    public PersonBuilder withLink(String link) {
        this.links.add(new Link(link));
        return this;
    }

    /**
     * Sets the list of link of the person that we are building.
     */
    public PersonBuilder withLinks(List<String> links) {
        this.links = new UniqueList<Link>().setItems(links.stream().map(Link::new).collect(Collectors.toList()));
        return this;
    }

    /**
     * Sets the {@code GraduatingYear} of the {@code Person} that we are building.
     */
    public PersonBuilder withGraduatingYear(String graduatingYear) {
        this.graduatingYear = new GraduatingYear(graduatingYear);
        return this;
    }

    /**
     * Sets the {@code Course} of the {@code Person} that we are building.
     */
    public PersonBuilder withCourse(String course) {
        this.course = new Course(course);
        return this;
    }

    /**
     * Sets the {@code Specialisation} of the {@code Person} that we are building.
     */
    public PersonBuilder withSpecialisation(String specialisation) {
        this.specialisation = new Specialisation(specialisation);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the emails of the person that we are building.
     */
    public PersonBuilder withEmails(List<String> emails) {
        this.emails = new UniqueList<Email>().setItems(emails.stream().map(Email::new).collect(Collectors.toList()));
        return this;
    }

    /**
     * Sets the {@code Pmail} of the {@code Person} that we are building.
     */
    public PersonBuilder withPriority(String priority) {
        this.priority = new Priority(priority);
        return this;
    }

    public Person build() {
        return new Person(name, phone, emails, links, graduatingYear, course, specialisation, tags, priority);
    }

}
