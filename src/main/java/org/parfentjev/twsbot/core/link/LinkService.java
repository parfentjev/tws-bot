package org.parfentjev.twsbot.core.link;

import org.parfentjev.twsbot.core.exceptions.DatabaseHelperException;
import org.parfentjev.twsbot.misc.DbHelper;

import java.util.Optional;

public class LinkService {
    private final DbHelper<Link, String> dbHelper = new DbHelper<>(Link.class);

    public Optional<Link> getLinkByUrl(String id) {
        return dbHelper.queryById(id);
    }

    public void saveLink(Link link) throws DatabaseHelperException {
        dbHelper.save(link);
    }
}
