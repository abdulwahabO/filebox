package io.github.abdulwahabo.filebox.services;

import io.github.abdulwahabo.filebox.model.Location;

public interface IPLookupService {

    /**
     *
     * @param ip
     * @return
     */
    Location lookup(String ip);
}
