package mops.portfolios.domain.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

  @Autowired
  EntryRepository entryRepository;

  @Autowired
  EntryFieldRepository entryFieldRepository;
}
