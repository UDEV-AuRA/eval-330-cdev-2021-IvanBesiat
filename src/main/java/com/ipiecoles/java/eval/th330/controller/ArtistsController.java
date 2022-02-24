package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.service.AlbumService;
import com.ipiecoles.java.eval.th330.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping(value ="/artists")
public class ArtistsController {

    @Autowired
    private ArtistService artistsvc;
    @Autowired
    private AlbumService albumsvc;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}"
    )
    public ModelAndView getArtistById(@PathVariable(value = "id") Long id){
        ModelAndView modelAndView = new ModelAndView("detailArtist");
        Artist artist = artistsvc.findById(id);
        modelAndView.addObject("art", artist);
        return modelAndView;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            params = "name"
    )
    public ModelAndView getArtistByName(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(defaultValue = "name") String sortProperty,
                                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
                                        @RequestParam String name) {
        ModelAndView artModelAndView = new ModelAndView("listeArtists");
        Page<Artist> artists = artistsvc.findByNameLikeIgnoreCase(name,page,size,sortProperty,sortDirection.toString());
        artModelAndView.addObject("start", page * size + 1);
        artModelAndView.addObject("end", page * size + artists.getNumberOfElements());
        artModelAndView.addObject("items", artists);
        return artModelAndView;
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ModelAndView listAllArtists(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(defaultValue = "name") String sortProperty,
                                        @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {
        ModelAndView allArtistsModelAndView = new ModelAndView("listeArtists");
        Page<Artist> artists = artistsvc.findAllArtists(page,size,sortProperty,sortDirection.toString());
        allArtistsModelAndView.addObject("start", page * size + 1);
        allArtistsModelAndView.addObject("end", page * size + artists.getNumberOfElements());
        allArtistsModelAndView.addObject("items", artists);
        return allArtistsModelAndView;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/new"
    )
    public ModelAndView createArtist() {
        ModelAndView modelAndView = new ModelAndView("detailArtist");
        modelAndView.addObject("art", new Artist());
        return modelAndView;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RedirectView insertArtist(Artist artist){
        if(artist.getId() == null){
            artistsvc.creerArtiste(artist);
            return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
        }
        else{
            artistsvc.updateArtiste(artist.getId(), artist);
            return new RedirectView("/artists/" + artist.getId());
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/delete"
    )
    public RedirectView deleteArtist(@PathVariable Long id){
        artistsvc.deleteArtist(id);
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{artistid}/album/{albumid}/delete"
    )
    public RedirectView deleteAlbum(@PathVariable Long artistid, @PathVariable Long albumid){
        albumsvc.deleteAlbum(albumid);
        return new RedirectView("/artists/{artistid}");
    }
}