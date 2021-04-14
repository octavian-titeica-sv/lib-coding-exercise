# Architecture

## UI Module
  - Contains app views (MainActivity and MainFragment) and the components involved in displaying the users (AlbumsAdapter
    and AlbumItemView).

## Presentation Module
  - Is the layer that interacts with the UI.
  - It contains the ViewModel.
  - ViewModel receives data from the repository and expose it to the UI using LiveData.

## Domain
  - This layer is using dependency inversion. The view model depends on AlbumsRepository abstraction
  rather than the concrete implementation.
  - For simplicity reasons, only a model class is used by this module and the upper modules (UI and presentation).
  - Also for simplicity reasons, the view model is directly calling the repository. In this case, an use case will act as a
  simple proxy between view model and repository, because no business logic is applied on the data.

## Repository
  - This layer holds the implementation of interfaces defined in domain layer: AlbumsRepository. This class
    emits the result upstream.
  - This module also contains the database and network implementation.
  - Database is implemented using Room. The cached data is retrieved using AlbumsDao class, being exposed as a flow.
  Db operations are performed on a separate model (AlbumDbModel) which has a dedicated mapper class called AlbumDbMapper,
  in order to perform mapping to and from AlbumModel.
  - Retrofit is used to fetch the remote data (albums). A specific networking model is dedicated for the remote response
  (AlbumNetworkModel). When the response is success, this model is being mapped into AlbumModel, using AlbumNetworkMapper
  class and then exposed a flow.
  - When the data is successfully fetched, it is saved into the database.

## App
 - This module holds the Application class and DI related classes.
 - Dagger components used for dependency injection are placed in dagger package of this module.

# Further recommendations:

1. Implement swipe to refresh in the Main Fragment. The refreshAlbums action is currently defined in AlbumsRepository and
implemented in AlbumsRepositoryImpl. Only the errors should be handled in the view model, because the db flow is still
activeand the data will be sent to the ui.
2. Use single live event for the error in view model. Currently, if an error occurred
and the device is rotated, the error snackbar will be displayed again.
3. Add unit tests for networking layer using MockWebServer. Some test jsons covering all happy and corner cases are needed.
This way, a safety net will be provided for networking part.
