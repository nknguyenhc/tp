package networkbook.testutil;

import java.util.Set;

import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.EditCommand;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.person.Email;
import networkbook.model.person.Person;
import networkbook.model.tag.Tag;
import networkbook.model.util.UniqueList;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return CreateCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(CliSyntax.PREFIX_NAME + " " + person.getName().fullName + " ");
        sb.append(CliSyntax.PREFIX_PHONE + " " + person.getPhone().value + " ");
        person.getEmails().stream().forEach(
                e -> sb.append(CliSyntax.PREFIX_EMAIL + " " + e.toString() + " ")
        );
        sb.append(CliSyntax.PREFIX_LINK + " " + person.getLink().getValue() + " ");
        sb.append(CliSyntax.PREFIX_GRADUATING_YEAR + " " + person.getGraduatingYear().value + " ");
        sb.append(CliSyntax.PREFIX_COURSE + " " + person.getCourse().value + " ");
        sb.append(CliSyntax.PREFIX_SPECIALISATION + " " + person.getSpecialisation().value + " ");
        person.getTags().stream().forEach(
            s -> sb.append(CliSyntax.PREFIX_TAG + " " + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditCommand.EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(CliSyntax.PREFIX_NAME).append(" ")
                                                    .append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(CliSyntax.PREFIX_PHONE).append(" ")
                                                    .append(phone.value).append(" "));
        if (descriptor.getEmails().isPresent()) {
            UniqueList<Email> emails = descriptor.getEmails().get();
            if (emails.isEmpty()) {
                sb.append(CliSyntax.PREFIX_EMAIL).append(" ");
            } else {
                emails.forEach(e -> sb.append(CliSyntax.PREFIX_EMAIL).append(" ")
                                                    .append(e.toString()).append(" "));
            }
        }
        descriptor.getLink().ifPresent(link -> sb.append(CliSyntax.PREFIX_LINK).append(" ")
                                                    .append(link.getValue()).append(" "));
        descriptor.getGraduatingYear().ifPresent(graduatingYear -> sb.append(CliSyntax.PREFIX_GRADUATING_YEAR)
                .append(" ").append(graduatingYear.value).append(" "));
        descriptor.getCourse().ifPresent(course -> sb.append(CliSyntax.PREFIX_COURSE).append(" ")
                .append(course.value).append(" "));
        descriptor.getSpecialisation().ifPresent(specialisation -> sb.append(CliSyntax.PREFIX_SPECIALISATION)
                .append(" ").append(specialisation.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(CliSyntax.PREFIX_TAG).append(" ");
            } else {
                tags.forEach(s -> sb.append(CliSyntax.PREFIX_TAG).append(" ")
                                                    .append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
