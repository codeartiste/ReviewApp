#Movie Reviews NYT
##Design
*Json document will be retrieved as a block of 20 records
*A linkedList will be used to cache the data in memory
*The images will be cached as and when it is rendered in a LRU cache with fixed size
*Json retrieval and fetching images are done using Async tasks
*MVP model is used to fetch and render the view
*MainActivity is the view that has a Listview with Image and textviews as elemnets
*ReviewPresenter class acts as the presentation layer
*ReviewModelData is the model for the app. It uses the caches or fetches data using HTTP
*DynamicListAdapter class has been subclassed to handle the getview method



##Tradeoffs
*AsyncTasks has being used for this POC. Thread Handler should be used for future design for a more centralized approach
*Network up and down handling needs refinements and are not handled
*"has_more" json attribute has not been checked for the next page
*Fixed number of reviews (300)has been used to populate the List dynamically
*Image LRU cache is 1/5th of the available JVM memory
*Disk caching has been ignored for this prototype
*Abrupt network Up and down has not been handled for all use cases
*HTTP error codes especially 429 has not been handled
*Screen rotation and Activity destruction handling is very basic as it repopulates the list
*Image population is done sequentially after the next list is populated


##Future Design enhancements:
*Use Diskcache for caching the last viewed list and populating the same for next launch
*Handle Network status from the activity
*OnResume and OnDestroy for Activity to be handled for better user experience
*Handle all http errors
*Have 2 separate threads for handling Listcache and Image cache 









