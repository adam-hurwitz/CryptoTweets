# CryptoTweets
Sample app displaying cryptocurrency related tweets. 

![CryptoTweets gif image](CryptoTweets.gif)

# Setup 

## Authentication

1. [Signup][1] for Twitter developer account.
    
2. [Create][2] a _Bearer Token_ for the Android app.
    
3. Insert the token as a request header in Retrofit.

   A. Key: `Authorization` 
   
   B. Value format: `Bearer {INSERT_YOUR_TOKEN}`
   
   C. In an _Auth.kt_ file under the following path: _CryptoTweets > app > src > main > java > app > cryptotweets_. 
   
   D. Add _Auth.kt_ to the _gitignore_ file(s).   

## Sample request
Get Twitter list: https://api.twitter.com/1.1/lists/statuses.json?list_id=1173707664863350785&count=10&page=1

# Libraries used

- [Navigation component][3]
- [Dagger][4]
- [ViewModel][5]
- [Retrofit][6]
- [Resource generic][7]
- [Room database][8]
- [Kotlin Coroutines Flow][9]
- [Data binding][10]
- [Glide][11]
- [Paging Library][12]
    
[1]:https://developer.twitter.com/en/apply-for-access
[2]:https://developer.twitter.com/en/docs/basics/authentication/oauth-2-0/bearer-tokens
[3]:https://developer.android.com/guide/navigation
[4]:https://developer.android.com/training/dependency-injection
[5]:https://developer.android.com/topic/libraries/architecture/viewmodel
[6]:https://square.github.io/retrofit/
[7]:https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt
[8]:https://developer.android.com/training/data-storage/room
[9]:https://kotlinlang.org/docs/reference/coroutines/flow.html
[10]:https://developer.android.com/topic/libraries/data-binding
[11]:https://github.com/bumptech/glide 
[12]:https://developer.android.com/topic/libraries/architecture/paging
