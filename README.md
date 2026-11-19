# ASU FindBack

An Android lost-and-found app for the Ain Shams University community.

ASU FindBack gives students, academic staff, and employees one place to report lost items, publish found items, search through reports, and claim belongings, instead of relying on scattered group chats and notice boards.


---

## The Idea

On a busy campus, phones, wallets, keys, and ID cards get misplaced all the time. Today the information is spread across informal channels, so people who lost something don't know where to look, and people who found something don't know who to contact.

ASU FindBack solves this with a single mobile app where:

1. Someone who **lost** an item creates a lost report.
2. Someone who **found** an item publishes a found report.
3. Anyone can **browse and search** the reports.
4. The owner of an item **claims** it with a message, and the person who posted the report **accepts or rejects** the claim.
5. Everyone involved gets an **in-app notification** at each step.

## Features

- **Authentication:** sign up and sign in; the session is kept locally, and logout is available.
- **Lost & Found reports:** item name, description, location, date, contact info, and an optional photo (camera or gallery). Future dates are not allowed.
- **Browse & filter:** home screen grid with All / Found / Lost filters, plus "my lost" and "my found" views.
- **Search:** type a keyword on the home screen and tap the search button. The app returns every report whose **name**, **description**, or **location** contains the keyword (partial, case-insensitive match). Results appear as a single-column list, and tapping one opens its details. When a search is active it covers all reports, regardless of the selected filter.
- **Item details:** full report view with a button to claim the item.
- **Claims:** a claimant must write a message, and users can't claim their own reports. Each claim is `PENDING`, `ACCEPTED`, or `REJECTED`.
- **Received claims:** report owners review claims on their items. Accepting one claim automatically rejects the other pending claims for that item.
- **Notifications:** created when a report is saved, a claim is submitted, and a claim is accepted or rejected. Includes an unread badge and mark-as-read.
- **Profile:** view account info and update name, phone number, and password.

## Tech Stack

| Area | Technology |
|---|---|
| Language | Kotlin |
| Platform | Native Android (min SDK 24, Android 7.0+) |
| UI | Activities with XML layouts, Material components |
| Local database | Room (SQLite) |
| Async work | Kotlin coroutines |

## Data Model

The `AppDatabase` class exposes one DAO per entity:

| Entity | Main attributes |
|---|---|
| `User` | id, name, email, password, phone number, faculty |
| `Items` | id, name, description, photo, date, location, contact info, owner, type (lost/found) |
| `Claim` | id, item, claimant, claimant name, message, status |
| `NotificationEntity` | id, user, title, message, type, related item, created-at, is-read |

Items reference their owner, and claims reference both the item and the claimant, with cascading deletes so dependent records are removed together.

## Main Screens

| Screen | Purpose |
|---|---|
| Welcome | Entry point |
| Sign up / Sign in | Account creation and login |
| Home | Browse, filter, and search reports |
| Add item | Create a lost or found report |
| Item details | View a report and start a claim |
| Claim | Submit a claim with a message |
| Received claims | Accept or reject claims on your items |
| Notifications | View and manage notifications |
| Profile / Update profile | View and edit account info |

## Getting Started

1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   ```
2. Open the project in **Android Studio**.
3. Let Gradle sync finish.
4. Run the app on an emulator or a physical device running Android 7.0 (API 24) or higher.

## Known Limitations & Future Work

- Data is stored **locally on the device**, so reports are not shared between devices yet. A shared backend is the next major step.
- Passwords are currently stored as plain text and should be hashed before any real deployment.
- Planned: better search filters, automatic matching between lost and found reports, image-based search, and usability testing with students and staff.

## Team

- Khalid Abdelhamid Hosni Abdelhamid
- Ahmed Khalil Mohamed El-Araby Mohamed
- Soha Sayed Refaay Abdelhamid
- Hoda Samy Abdo Zaki
- Sara Abdelhamid Abdelazim Abdelhamid
- Samaa Mohamed Abdallah Mahdy

## Documentation

A two-page project abstract in ACM format is included in the repository: `ASU_FindBack_ACM_Abstract.pdf`.
