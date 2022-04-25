currentGitCommit=$(git describe --long)
if [ $# -eq 0 ]; then
	version=$currentGitCommit
		jpackage -i ../jar -d ../deb --main-jar DecompressionPlanner-$version.jar -n 'Decompression Planner' --verbose --linux-shortcut --add-launcher 'Decompression Planner for high DPI screens'=highDPI.properties --app-version $version
else
	version=$1
	git checkout $1
	version=$(git describe --long)
	jpackage -i ../jar -d ../deb --main-jar DecompressionPlanner-$version.jar -n 'Decompression Planner' --verbose --linux-shortcut --add-launcher 'Decompression Planner for high DPI screens'=highDPI.properties --app-version $version		
fi
git checkout $currentGitCommit
