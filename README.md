# Device & Infrastructure Mapper

**Mapping of Nearby Devices and Cellular Infrastructure** (aka Device Mapping App)  
An Android application built with **Kotlin**, utilizing Google Maps, BLE, Wi-Fi scanning, local network discovery, and OpenCelliD for telecom tower mapping.

---

##  Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
- [Saving Scan History](#saving-scan-history)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

This app helps users scan and visualize nearby electronic devices—including cameras, mobile phones, wearables—plus telecom base stations on a dynamic map. It combines:
- **BLE scanning** for advertisements
- **LAN discovery** via TCP socket scanning
- **Cell tower mapping** using OpenCelliD public API
- Persistent history using **Room + Paging 3**

Built for educational and exploratory use with only free tools and APIs.

---

## Features

-  Real-time map showing device and tower markers
-  Filter by Wi-Fi (LAN), Bluetooth, Cell towers, or show All
-  Export scan results as **CSV** and **PDF**
-  Paginated scan history with persistence
-  Light/Dark theme toggle via navigation drawer settings

---

## Getting Started

### Prerequisites

- Android Studio (latest stable)
- Android device with BLE + Wi-Fi
- Google Maps SDK API key
- OpenCelliD API key for cell tower lookup

### Installation

1. Clone the repo:
   ```bash
   git clone <https://github.com/khush196/MyAndroidApp.git>

2. Open project in Android Studio

3. Add API keys to local.properties:

Sync Gradle and build the project

Run on a BLE + Wi-Fi capable Android device

Usage
Launch the app and allow required permissions (location, Bluetooth, Wi-Fi).

Tap Start Scan to detect nearby devices and towers.

View markers on the map and tap them for details.

Export results via the navigation drawer menu for offline use.

Access your scan history via the History menu for paginated past scans.

Saving Scan History
Every scan session is saved to a Room database, and displayed through a RecyclerView using Paging 3. This ensures efficient scrolling and performance even with large history data.

Screenshots
(Include .png images: map view, filters, export UI, history list)

Contributing
Contributions and enhancements are welcome!
Feel free to:

Open an issue to discuss features or bugs

Fork this repo and submit pull requests

Please reference the Contributor Covenant for best practices.

License
This project is licensed under the MIT License — see the LICENSE file for details.




Thank you for exploring the Device & Infrastructure Mapper. Your feedback and use are appreciated!
---  
This version combines real-world structure from professional README guides :contentReference[oaicite:1]{index=1} with appropriate sections customized to your app's architecture and functionality.  
Let me know if you'd like refinements or add a live demo section!
::contentReference[oaicite:2]{index=2}
