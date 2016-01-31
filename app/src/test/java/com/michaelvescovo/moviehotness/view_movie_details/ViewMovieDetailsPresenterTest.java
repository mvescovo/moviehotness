/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * I've changed the code but it's similar to the example I'm following.
 * Possibly the best Android example I've ever seen so far. Really nice design.
 *
 * https://codelabs.developers.google.com/codelabs/android-testing
 *
 */

package com.michaelvescovo.moviehotness.view_movie_details;

import android.content.Context;

import com.michaelvescovo.moviehotness.model.Movie;
import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.michaelvescovo.moviehotness.model.MovieRepository;
import com.michaelvescovo.moviehotness.model.MovieTrailerInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by Michael Vescovo on 9/01/16.
 *
 */
public class ViewMovieDetailsPresenterTest {

    private static final String INVALID_ID = "INVALID_ID";
    private static final String ID_TEST = "286217";
    private static final String TITLE_TEST = "The Martian";
    private static final String RELEASE_DATE_TEST = "2015-09-11";
    private static final String POSTER_URL_TEST = "/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg";
    private static final String VOTE_AVERAGE_TEST = "7.67";
    private static final String PLOT_TEST = "During a manned mission to Mars, Astronaut Mark Watney is presumed dead after a fierce storm and left behind by his crew. But Watney has survived and finds himself stranded and alone on the hostile planet. With only meager supplies, he must draw upon his ingenuity, wit and spirit to subsist and find a way to signal to Earth that he is alive.";
    private static final String BACKDROP_URL_TEST = "/sy3e2e4JwdAtd2oZGA2uUilZe8j.jpg";

    private static final MovieInterface MOVIE_TEST = new Movie(ID_TEST, TITLE_TEST, RELEASE_DATE_TEST, POSTER_URL_TEST, VOTE_AVERAGE_TEST, PLOT_TEST, BACKDROP_URL_TEST);

    public static final String AUTHOR = "Frank Ochieng";
    public static final String CONTENT = "The Martian’ is definitely in the creative wheelhouse of filmmaker Ridley Scott whose Science Fiction sensibilities are grounded in colorful futuristic fantasies that tiptoe in grand whimsy.  The veteran auteur responsible for such pop cultural high-minded spectacles in ‘Alien’, ‘Blade Runner’ and even the mixed bag reception of ‘Prometheus’ certainly brings a sophisticated and thought-provoking vibe to the probing aura of ‘The Martian’. As with other Scott-helmed productions, ‘The Martian’ settles nicely in its majestic scope that taps into visual wonderment, humanistic curiosities, technical impishness and the surreal spryness of the SF experience.\\r\\n\\r\\nThe exploratory nature of ‘The Martian’ is its own noteworthy mission. Scott arms his frothy planetary odyssey with a sense of thematic inquisitiveness and intelligence while meshing human consciousness and scientific forethought. Clearly, ‘The Martian’ is astutely meditative and one can thankfully block in their memory banks some of Scott’s mediocre misses (anyone care to recall the monumental mishaps in the form of the flimsy ‘The Counselor’ or the unintentional laughfest that was ‘Exodus: Gods And Kings’?). Rightfully so, ‘The Martian’ shares its crafty crater of similarities with other space-place spectacles that resonated with the imagination and innovation such as the Academy Award-winning ‘Gravity’ and the under-rated ‘Interstellar’. \\r\\n\\r\\nQuite frankly, ‘The Martian’ is elegantly arresting in its sheer skin for both the character studies and the major plight involved (particularly anchored on the shoulders of star Matt Damon as the contemplative lead) as well as the trademark Scott-induced flourishes that incorporate crisp and cunning visual special effects and a sweeping musical score that invites a transfixing hold on one’s indelible spirit.\\r\\n\\r\\n‘The Martian’ is the film adaptation of Andy Weir’s best-selling tome. Scott’s disciplined direction and screenwriter Drew Goddard’s ambitious vision of Weir’s compelling written material seemingly gels in convincing, cerebral fashion. The premise is not wildly original as it revisits the familiar foundation of a lost soul in survival mode trying to take on the treacherous surroundings with philosophical conviction. Nevertheless, this does not make ‘The Martian’ any less formidable in its soul-searching perspective. Scott’s narrative on individualism and isolation with a tremendous technological tilt truly registers with boisterous bounce. Amazingly, Scott has assembled a capable and committed cast that are dedicated to making ‘The Martian’ look resourcefully skillful in its masterful mischievousness. Sure, The Martian’s lengthy running time is staggering but it is compelling enough to invest in its meandering, adventurous narrative.\\r\\n\\r\\nThe ultimate nightmare (or perilous predicament…take your choice) has been realised for one stranded space traveler in Mark Watney (Matt Damon). You see…poor astronaut Watney had the misfortune of losing contact with his commander in Melissa Lewis (Jessica Chastain) and the rest of his crew when undergoing a scientific expedition to examine the terrain on Mars. Courtesy of an unexpected freakish storm Watney is separated from his comrades and believed to be dead as a result of the hazardous weather conditions on Mars. Lewis and her associates have no choice but to head back to Earth as searching for Watney may prove to be futile and endanger their lives as well.\\r\\n\\r\\nThus, Mark Watney is basically his own forced-upon version of ‘Robinson Crusoe On Mars’. There is no option for Watney but to allow his major survival instincts to kick in while striving for his jeopardised livelihood on the unpredictable and treacherous Red Planet’s jagged landscape. Despite being stuck in hostile territory, Watney must use his background skills as a trained botanist to cultivate the scarce food he must rely on for his unconventional nutrition. The only ‘companion’ that Watney has to relate to rests in the recorded device that captures his harried thoughts through video diaries (at least this is more practical than what Tom Hanks’s ‘Castaway’ character had in his possession with inanimate object best buddy, Wilson the volleyball).\\r\\n\\r\\nIn the meanwhile, the devastating news about Mark Watney’s possible death far beyond the reaches of home hits his colleagues understandably hard as tributes start to pour in remembrance of the seemingly dearly departed space pioneer. Soon, there is some measure of hope when NASA officials that include head honchos Teddy Sanders (Jeff Daniels from TV’s ‘The Newsroom’), Vincent Kapoor (the Oscar-nominated Chiwetel Ejiofor from ’12 Years A Slave’) and Mitch Henderson (Sean Bean) find evidence that Watney may be alive and ready to be rescued. Naturally, a sense of urgency is warranted to retrieve the weary astronaut without causing too many public relations waves. This puts press aide Annie Montrose (Kristen Wiig) into damage control mode in particular.\\r\\n\\r\\nOkay...so it is not as scenic as Yellowstone National Park but the rocky region in THE MARTIAN still has some unassuming charm, right?\\r\\nOkay…so it is not as scenic as Yellowstone National Park but the rocky region in THE MARTIAN still has some unassuming charm, right?\\r\\nImportantly, ‘The Martian’ is clever and carefully conceived because it does not have to rely on the excess baggage to convey its entertainment value in a series of hyperactive and hedonistic happenings. Ridley Scott is soundly methodical in presenting a low-key terrifying tale of loneliness and resiliency and what it takes to handle the pressure of adversity when there is no glimmer of humanity around to reinforce or remind one of such psychological obstacles. The intriguing factor here is the science-friendly serving of intelligence and insight that builds up the audience’s appreciation for the underlying suspense. Interestingly, the conflict approached in ‘The Martian’ is not so much the back-and-forth high-scale struggles of man versus outlandish creature or an over-the-top diabolical deviant out to destroy the world in cartoonish fashion. No, the genuine combative and confrontational war of will is contained in one vulnerable man’s ability to face the unknown grounded in both doubt and determination. Hence, Scott has made a palpable thinking man’s Science Fiction exposition rooted in articulated forethought.\\r\\n\\r\\nDamon more than holds his own as the disillusioned botanist/astronaut out on an ominous limb as his doomed odyssey of uncertainty is something that the common viewer can embrace and relate to without question. The perceived opulence in ‘The Martian’ is not contained in the film’s production values per se (Scott has made previous movies with more visual vitality and purpose) but subtly showcased in the concepts of  knowledgeable tidbits concerning food rations, mathematical equations, crucial time tables, planetary probes, NASA-themed procedural techniques and aerodynamics considerations.\\r\\n\\r\\nHopefully, ‘The Martian’ is not dismissed as a foreign alien in the minds of ardent movie-going SF enthusiasts looking for a realistic and soulful space-age adventure with a refreshing backbone of scientific curiosity and candidness.\\r\\n\\r\\nThe Martian (2015)\\r\\n\\r\\n20th Century Fox\\r\\n\\r\\n2 hrs. 35 mins.\\r\\n\\r\\nStarring: Matt Damon, Jessica Chastain, Jeff Daniels, Kristen Wiig, Michael Pena, Sean Bean, Chiwetel Ejiofor, Kate Mara, Sebastian Stan, Donald Glover and Benedict Wong\\r\\n\\r\\nDirected by: Ridley Scott\\r\\n\\r\\nMPAA Rating: PG-13\\r\\n\\r\\nGenre: Science Fiction/Space Adventure/Science and Fantasy\\r\\n\\r\\nCritic’s rating: *** 1/2 stars (out of 4 stars).";

    @Mock
    private MovieRepository mMovieRepository;

    @Mock
    private ViewMovieDetailsContract.View mViewMovieDetailsView;

    @Mock
    private Context mContext;

    @Mock
    private ArrayList<MovieTrailerInterface> mTrailers;

    @Captor
    private ArgumentCaptor<MovieRepository.GetMovieCallback> mGetMovieCallbackCaptor;

    private ViewMovieDetailsPresenter mViewMovieDetailsPresenter;

    @Before
    public void setupViewMovieDetailsPresenter() {
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mViewMovieDetailsPresenter = new ViewMovieDetailsPresenter(mContext, mMovieRepository, mViewMovieDetailsView);
    }

    @Test
    public void getMovieFromRepositoryAndLoadIntoView() {
        // Given an initialised ViewMovieDetailsPresenter with stubbed movie
        Movie movie = new Movie(ID_TEST, TITLE_TEST, RELEASE_DATE_TEST, POSTER_URL_TEST, VOTE_AVERAGE_TEST, PLOT_TEST, BACKDROP_URL_TEST);

        // When ViewMovieDetailsPresenter is asked to load movie
        mViewMovieDetailsPresenter.loadMovieDetails(movie.getId(), false);

        // Then the movie is loaded from the model, a callback is captured and the progress indicator is shown
        verify(mViewMovieDetailsView).setProgressIndicator(true);
        verify(mMovieRepository).getMovie(eq(mContext), eq(movie.getId()), mGetMovieCallbackCaptor.capture());

        // When movie is finally loaded
        mGetMovieCallbackCaptor.getValue().onMovieLoaded(movie);

        // Then progress indicator is hidden and details are shown in UI
        verify(mViewMovieDetailsView).setProgressIndicator(false);
        verify(mViewMovieDetailsView).showMovieDetails(movie);
    }

    @Test
    public void getUnknownMovieFromRepositoryAndLoadIntoView() {
        // When loading a movie is requested with an invalid movie ID.
        mViewMovieDetailsPresenter.loadMovieDetails(INVALID_ID, false);

        // Then the movie with invalid id is attempted to load from model, callback is captured and progress indicator is shown.
        verify(mViewMovieDetailsView).setProgressIndicator(true);
        verify(mMovieRepository).getMovie(eq(mContext), eq(INVALID_ID), mGetMovieCallbackCaptor.capture());

        // When movie is finally loaded
        mGetMovieCallbackCaptor.getValue().onMovieLoaded(null);

        // Then progress indicator is hidden and missing movie UI is shown
        verify(mViewMovieDetailsView).setProgressIndicator(false);
        verify(mViewMovieDetailsView).showMissingMovie();
    }

    @Test
    public void getFirstTrailerAndCallYouTube() {
        // When loading the trailer is requested
        mViewMovieDetailsPresenter.playFirstTrailer(anyString());

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showFirstTrailerUi(anyString());
    }

    @Test
    public void getFullPlotAndLoadIntoView() {
        // When loading the full plot is requested
        mViewMovieDetailsPresenter.openFullPlot(TITLE_TEST, PLOT_TEST);

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showFullPlotUi(TITLE_TEST, PLOT_TEST);
    }

    @Test
    public void getAllTrailersAndLoadIntoView() {
        // When loading all trailers is requested
        mViewMovieDetailsPresenter.openAllTrailers(mTrailers);

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showAllTrailersUi(mTrailers);
    }

    @Test
    public void getFullReviewAndLoadIntoView() {
        // When loading the full review is requested
        mViewMovieDetailsPresenter.openFullReview(AUTHOR, CONTENT);

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showFullReview(AUTHOR, CONTENT);
    }

    @Test
    public void getAttributionAndLoadIntoView() {
        // When loading attribution is requested
        mViewMovieDetailsPresenter.openAttribution();

        // Then the view is called to update the view
        verify(mViewMovieDetailsView).showAttributionUi();
    }

    @Test
    public void checkMovieIsFavouriteAndShowFab() {
        /*
        * No mock available to test this. Need to use instrumentation test.
        *
        * */

        // When loading favourite fab is requested
//        mViewMovieDetailsPresenter.loadFavouriteFab(ID_TEST);

        // Then the view is called to update the view
//        verify(mViewMovieDetailsView).setFavouriteFab(R.drawable.ic_favorite_white_24dp, true);
    }

    @Test
    public void addFavouriteMovieAndShowSnackbar() {
        /*
        * No mock available to test this. Need to use instrumentation test.
        *
        * */

        // When adding favourite is requested
//        mViewMovieDetailsPresenter.addFavouriteMovie(MOVIE_TEST);

        // Then the view is called to update
//        verify(mViewMovieDetailsView).showSnackbar(R.string.fragment_detail_favourite_added);

        // Then change the FAB to allow the reverse operation
//        verify(mViewMovieDetailsView).setFavouriteFab(R.drawable.ic_remove_24dp, false);
    }

    @Test
    public void removeFavouriteMovieAndShowSnackbar() {
        /*
        * No mock available to test this. Need to use instrumentation test.
        *
        * */

        // When removing favourite is requested
//        mViewMovieDetailsPresenter.removeFavouriteMovie(ID_TEST);

        // Then the view is called to update
//        verify(mViewMovieDetailsView).showSnackbar(R.string.fragment_detail_favourite_removed);

        // Then change the FAB to allow the reverse operation
//        verify(mViewMovieDetailsView).setFavouriteFab(R.drawable.ic_favorite_white_24dp, true);
    }
}
