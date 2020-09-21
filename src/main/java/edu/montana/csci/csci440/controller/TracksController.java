package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Artist;
import edu.montana.csci.csci440.model.Track;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class TracksController {
    public static void init() {
        /* CREATE */
        get("/tracks/new", (req, resp) -> {
            Track track = new Track();
            return Web.renderTemplate("templates/tracks/new.vm", "album", track);
        });

        post("/tracks/new", (req, resp) -> {
            Track track = new Track();
            Web.putValuesInto(track, "Name", "Milliseconds", "Bytes", "UnitPrice");
            if (track.create()) {
                Web.message("Created A Track!");
                return Web.redirect("/tracks/" + track.getTrackId());
            } else {
                Web.error("Could Not Create A Track!");
                return Web.renderTemplate("templates/tracks/new.vm",
                        "track", track);
            }
        });

        /* READ */
        get("/tracks", (req, resp) -> {
            List<Track> tracks = Track.all(Web.getPage(), Web.PAGE_SIZE);
            return Web.renderTemplate("templates/tracks/index.vm",
                    "tracks", tracks);
        });

        get("/tracks/:id", (req, resp) -> {
            Track track = Track.find(Integer.parseInt(req.params(":id")));
            return Web.renderTemplate("templates/tracks/show.vm",
                    "track", track);
        });

        /* UPDATE */
        get("/tracks/:id/edit", (req, resp) -> {
            Track track = Track.find(Integer.parseInt(req.params(":id")));
            return Web.renderTemplate("templates/tracks/edit.vm",
                    "track", track);
        });

        post("/tracks/:id", (req, resp) -> {
            Track track = Track.find(Integer.parseInt(req.params(":id")));
            Web.putValuesInto(track, "Name", "Milliseconds", "Bytes", "UnitPrice");
            if (track.update()) {
                Web.message("Updated Track!");
                return Web.redirect("/tracks/" + track.getTrackId());
            } else {
                Web.error("Could Not Update Track!");
                return Web.renderTemplate("templates/tracks/edit.vm",
                        "track", track);
            }
        });

        /* DELETE */
        get("/tracks/:id/delete", (req, resp) -> {
            Track track = Track.find(Integer.parseInt(req.params(":id")));
            track.delete();
            Web.message("Deleted Track " + track.getName());
            return Web.redirect("/tracks");
        });
    }
}
