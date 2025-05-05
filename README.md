# 🎧 SoundMatch - Music Explorer (Incomplete Project)

**SoundMatch** is an Android application developed with the goal of implementing **automatic equalization by genre**, inspired by Spotify. It focuses on modern Android development practices and aims to provide a clean, fluid, and intuitive user experience.

---

## 🚧 Project Status: Incomplete

While many features have been implemented, **music playback is not fully operational** due to recent Spotify API restrictions (thanks Spotify btw). Despite this, the project is a strong exercise in architecture, UI design, and problem-solving using current tools and libraries.

---

## 🛠️ Key Technologies Used

- **Architecture**: MVVM for separation of concerns and better testability
- **Dependency Injection**: Hilt
- **UI**: Jetpack Compose
- **Navigation**: Compose Navigation
- **Networking**: Retrofit
- **Audio Playback**: ExoPlayer (limited by API restrictions)
- **Pagination**: Paging 3 for efficient data loading

---

## 🔍 Development Process & Challenges

### 🎯 Deep Learning & Dedication
This project was a personal deep dive into Android architecture, modern UI, and Spotify API integration. Countless hours went into learning and building.

### 🔐 Spotify API Limitations
Playback is limited due to:
- Restrictions on `preview_url` availability.
- Requirement of a **Spotify Premium account** for full playback access.
- Deprecation of certain previously available endpoints.

### 🧪 CSV Preview URL Workaround
To overcome these limitations, I experimented with a workaround:
- ✅ A **300MB CSV** dataset of songs was used.
- 🛠️ A **PowerShell script** enriched the dataset with `preview_url`s.
- 🔎 Custom logic in the `SearchViewModel` and `CsvUtils` handled song lookups.

This solution partially worked but proved hard to maintain due to the scale and dynamic nature of the data.

### ⚠️ Authentication Issues
OAuth 2.0 was partially implemented, but performance issues and Spotify's Premium-only access policies made this feature impractical.

---

## 🔬 What This Project Shows

- Well-structured architecture and modular design.
- Use of **Jetpack Compose** for reactive UIs.
- Integration of **Spotify’s API** for metadata (artists, albums, tracks).
- Implementation of **pagination** for large search result sets.
- Creative thinking when facing external limitations.

---

## 🖼️ Screenshots
*Coming soon...*

---

## 🔮 Next Steps (If Continued)

- Consider alternative music APIs with fewer playback limitations.
- Focus on discovery, playlists, or genre-based recommendations instead of direct playback.
- Explore Firebase or Supabase for user accounts and cloud-hosted metadata.

---

## ✅ Why Feature an Incomplete Project?

Even without full playback, **SoundMatch is a testament to my skills**, problem-solving, and persistence when facing limitations out of my control. I’m proud of what I built and what I learned—and I hope it reflects my passion for mobile development.

---

Thanks for checking it out! 🙌
