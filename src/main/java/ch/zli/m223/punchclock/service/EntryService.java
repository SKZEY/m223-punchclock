package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.repository.EntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EntryService {
    private EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public Entry createEntry(Entry entry) {
        if (checkEntryDates(entry)) {
               return entryRepository.saveAndFlush(entry);
        }
        return entry;
    }

    public void deleteEntry(Long id) { this.entryRepository.deleteById(id); }

    public List<Entry> findAll() {
        return entryRepository.findAll();
    }

    private boolean checkEntryDates(Entry entry) throws ResponseStatusException {
        if (entry.getCheckIn().isAfter(entry.getCheckOut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }
        else
        {
            return true;
        }
    }
}
