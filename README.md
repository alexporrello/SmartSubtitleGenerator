# SmartSubtitleGenerator
This is a subtitle exporter app and script writing app, developed by Alexander Porrello in Java with JModern Library for the YouTube channel GamingSins.

# Getting Started with the application
1. Click File > New Project (or the shortcut 'ctrl + N'). Select the save location of the subtitle file. Once you have selected the project location and pressed the 'Save' button, the new project file will be loaded.
    * To load a file, click File > Open Project. A list of the six most recently opened files can be accessed by hovering over File > Open Recent and selecting the file you wish to open. 
2. The area where new subtitles are entered is at the bottom of the window. The subtitle’s timecode will be entered to the far left. The subtitle’s text will be entered in the middle. The button with the right arrow (>) icon adds the subtitle to the project. The button with the x icon quickly clears both the timecode and the text area. There are two methods for entering a timecode:
    * Manually enter the timecode (hh:mm:ss) of the subtitle location in your video editing software.
    * If you are editing with Adobe Premiere® Pro CC, click on Window > Paste time from Adobe Premiere® Pro CC. Once this option is selected, copy the timecode directly from Adobe Premiere® Pro CC and click on the field where you enter the subtitle’s text. Smart Subtitle Generator will auto-paste the timecode. (To revert to manual entry, click Window > Paste time from Adobe Premiere® Pro CC).
3. Once the subtitle’s time and text have been entered, press on the button with the right arrow (>) icon to load it into the project (or press your keyboard's 'Enter' key). The subtitle will appear in the above window, and the subtitle-entry area will be cleared. Repeat until all of the subtitles have been entered.
    * Please note that subtitles are automatically sorted by their timecode. If you would like to change an existing subtitle’s timecode, double click on its hours, minutes, or seconds field and enter a new number.
4. To export subtitles that can be imported into your editing software, (1) select Subtitle > Export Subtitles, (2) select the location where you would like the subtitles to be exported, and (3) once the location has been selected, press the 'Open' button. All of the subtitles will auto-export.
    * Subtitles are by default placed on a black bar with a background opacity of .75. To change the black bar’s background opacity, hover over Subtitle > Overlay Opacity and click the desired value.
    * Please note that exported subtitle files are named by their timecode. If you have changed a subtitle's timecode, you will have to re-import the overlay into your video editing software.

