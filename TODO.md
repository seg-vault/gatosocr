# To Do

- ~~store files that are currently being worked on and check against this store to prevent duplicate
jobrunr instances working on the same file. Add logic at the start of the for(Path path : paths) 
in OcrService to check the store and skip doing work on a path if it exists in the store.~~ 09/30/22

- Convert PDF to Tiff so PDF's can be supplied to app.

- If Convert to Tiff works, add dyllanwli to README attribution as a thank you for the gist - https://gist.github.com/dyllanwli/58fe0db862c823ebfc2fde1f8678d14c