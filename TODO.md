# To Do

- store files that are currently being worked on and check against this store to prevent duplicate
jobrunr instances working on the same file. Add logic at the start of the for(Path path : paths) 
in OcrService to check the store and skip doing work on a path if it exists in the store.