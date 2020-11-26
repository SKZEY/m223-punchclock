package ch.zli.m223.punchclock.security;

import ch.zli.m223.punchclock.domain.ApplicationUser;
import ch.zli.m223.punchclock.domain.Category;
import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.repository.CategoryRepository;
import ch.zli.m223.punchclock.repository.EntryRepository;
import ch.zli.m223.punchclock.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SeedTestData {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;
    private EntryRepository entryRepository;
    private CategoryRepository categoryRepository;

    public SeedTestData(
            UserRepository userRepository,
            BCryptPasswordEncoder pwencoder,
            EntryRepository entryRepository,
            CategoryRepository categoryRepository
    ) {
        this.userRepository = userRepository;
        this.encoder = pwencoder;
        this.entryRepository = entryRepository;
        this.categoryRepository = categoryRepository;
    }

    @EventListener
    public void seedData(ApplicationReadyEvent even) {
        ApplicationUser user = new ApplicationUser();
        user.setPassword(encoder.encode("12345678"));
        user.setUsername("admin");
        userRepository.save(user);

        Category category = new Category();
        category.setName("Arbeit");

        Category category1 = new Category();
        category1.setName("Schule");

        categoryRepository.saveAndFlush(category);
        categoryRepository.saveAndFlush(category1);




        Entry entry = new Entry();

        String str = "2020-03-04 08:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        String str1 = "2020-03-04 16:00";
        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);

        entry.setCheckIn(dateTime);
        entry.setCheckOut(dateTime1);

        entry.setApplicationUser(user);
        entry.setCategory(category);

        entryRepository.saveAndFlush(entry);
    }
}
