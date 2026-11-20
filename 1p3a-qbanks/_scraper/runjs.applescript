on run argv
	set jsPath to item 1 of argv
	set urlMatch to item 2 of argv
	set jsCode to read POSIX file jsPath as «class utf8»
	tell application "Google Chrome"
		repeat with w in windows
			repeat with t in tabs of w
				try
					if (URL of t) contains urlMatch then
						return execute t javascript jsCode
					end if
				end try
			end repeat
		end repeat
	end tell
	return "NOT_FOUND"
end run
