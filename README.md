# SoundMatch - Music Explorer (Incomplete Project)

**SoundMatch** is an Android application developed with the intention of the use for an Automatic EQ, inspired by the popular platform Spotify. This project was built with a focus on **Android development best practices** and the utilization of **modern technologies**, aiming to deliver a fluid and intuitive user experience.

**Project Status:**

This project is **incomplete**. While significant effort was dedicated to its development and various functionalities were implemented, **music playback is not fully operational** due to **recent restrictions and changes in the Spotify API** (thanks Spotify btw). Despite this limitation, SoundMatch represents a comprehensive exercise in applying cutting-edge architectures and technologies in Android development.

## Key Technologies Used:

* **Architecture:** Model-View-ViewModel (MVVM) for clear separation of concerns and improved testability.
* **Dependency Injection:** Hilt for efficient and straightforward management of dependencies throughout the application.
* **User Interface:** Jetpack Compose, the modern declarative UI toolkit from Android, to create attractive and reactive user interfaces.
* **Navigation:** Compose Navigation for fluid and declarative management of the navigation flow within the application.
* **Networking:** Retrofit for performing efficient and secure HTTP calls to the Spotify API.
* **Audio Playback:** ExoPlayer library was integrated as the foundation for audio playback functionality (currently limited by API restrictions).
* **Pagination:** Paging 3 to load and display large datasets efficiently, improving performance and user experience when exploring search results and extensive lists.

## Dedication and Challenges Overcome:

The development of SoundMatch was a process of deep learning and dedication. Considerable time was invested in understanding and implementing the various technologies mentioned. Despite the challenges encountered, significant progress was made in the application's structure and interface.

### Overcoming Spotify API Restrictions:

A major obstacle encountered during development was the **implementation of playback functionality due to recent restrictions imposed by the Spotify API**. These limitations affected the availability of certain resources necessary for direct playback without an active Spotify Premium account and the closure of some previously available features.

### The Attempt with CSV for Preview URLs:

Aware of the limitations in directly obtaining `preview_url` from the API for all users, an alternative solution was explored: **the integration of a local CSV file containing song metadata, including preview URLs.**

**CSV Creation and Implementation Process:**

1.  **Obtaining a Massive Dataset:** Work was done with a dataset of approximately **300MB of song data** in CSV format.
2.  **Enrichment with Preview URLs (via PowerShell):** A **PowerShell** script was developed to attempt to enrich this dataset with the corresponding `preview_url`s. This process involved the manipulation and processing of a large amount of data to associate preview URLs with the songs existing in the CSV.
3.  **Implementation in the Application:** Logic was implemented in the `SearchViewModel` and utilities (`CsvUtils`) to read this CSV file and look up `preview_url`s based on the song ID obtained from the Spotify API. This attempt aimed to provide preview functionality even with the API restrictions.

While this solution allowed progress in the desired direction, the **maintenance and accuracy of such a large and dynamic dataset presented significant challenges.**

### Challenges with Spotify Authentication:

An attempt was made to implement the **Spotify authentication flow (OAuth 2.0)** to allow users to connect their accounts. However, **serious performance issues** were encountered during this process. Furthermore, Spotify API policies imply that **only users with an active Spotify Premium account can use certain functionalities**, which would significantly limit the user base of SoundMatch. Due to these challenges and limitations, the complete implementation of authentication was not finalized.

Despite the limitations in playback, this project offers an excellent opportunity to review and evaluate the following areas:

* The overall **structure of the application** and how the MVVM pattern is applied.
* The **implementation of dependency injection** with Hilt.
* The use of **Jetpack Compose** to build the user interface and navigation.
* The **integration with the Spotify API** for searching and obtaining metadata (artists, albums, tracks, etc.).
* The **implementation of pagination** to handle large lists of results efficiently.
* The **attempt to integrate a CSV file** as a supplementary data source.

## Screenshots (Coming Soon):

## Next Steps (Considerations):

If this project were to be continued, the following alternatives could be considered:

* **Integration with a different music API** that offers more flexibility in playback for developers.
* **Focusing on the discovery and exploration features** of music, leaving aside direct playback.

## Conclusion:

SoundMatch, although incomplete in its playback functionality, represents a significant effort in applying modern architectures and technologies in Android development. This project demonstrates **my strong dedication, the ability to face technical challenges, and the exploration of creative solutions** in the face of limitations imposed by external services. Despite its current limitations, I hope it serves as **solid evidence of my skills and my commitment to developing quality Android applications.**
