if [ $# -eq 0 ]
	then
	version=$(git describe --tags)
		jpackage -i ../jar -d ../appimage --main-jar DecompressionPlanner-$version.jar -n 'Decompression Planner' --verbose --add-launcher 'Decompression Planner for high DPI screens'=highDPI.properties --app-version $version --type app-image
	else
		jpackage -i ../jar -d ../appimage --main-jar DecompressionPlanner-$1.jar -n 'Decompression Planner' --verbose --add-launcher 'Decompression Planner for high DPI screens'=highDPI.properties --app-version $1 --type app-image
fi
