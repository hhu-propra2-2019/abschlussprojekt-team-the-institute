package mops.portfolios.domain.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EntryService {

  @Autowired
  EntryRepository entryRepository;

  @Autowired
  EntryFieldRepository entryFieldRepository;

  public EntryField findFieldById(Entry entry, Long entryFieldId) {
    for(EntryField field : entry.getFields()) {
      if(field.getId() == entryFieldId) {
        return field;
      }
    }
    return null;
  }

  public void update(Entry entry) {
    entryRepository.save(entry);
  }
}
