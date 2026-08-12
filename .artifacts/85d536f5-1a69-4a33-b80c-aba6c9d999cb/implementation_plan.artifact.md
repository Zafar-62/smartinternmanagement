# Fix Warnings in ProgressActivity.kt

The goal is to resolve Lint warnings in `ProgressActivity.kt` and its associated layout by moving hardcoded strings to resources and improving code formatting.

## Proposed Changes

### [Component Name]

#### [MODIFY] [strings.xml](file:///C:/Users/hney3/AndroidStudioProjects/NTCInterns/app/src/main/res/values/strings.xml)
- Add string resources for the progress screen title and summary text.

#### [MODIFY] [activity_progress.xml](file:///C:/Users/hney3/AndroidStudioProjects/NTCInterns/app/src/main/res/layout/activity_progress.xml)
- Use `@string/progress_title` instead of hardcoded text.

#### [MODIFY] [ProgressActivity.kt](file:///C:/Users/hney3/AndroidStudioProjects/NTCInterns/app/src/main/java/com/zafainternational/ntcinterns/ProgressActivity.kt)
- Use `getString(R.string.progress_summary, ...)` to set the text.
- Add a trailing comma to the `findViewById` call to follow Kotlin style guidelines and resolve the Lint warning.

## Verification Plan

### Automated Tests
- Run `app:assembleDebug` to ensure the project still builds.

### Manual Verification
- None required as these are cosmetic/best-practice changes.
