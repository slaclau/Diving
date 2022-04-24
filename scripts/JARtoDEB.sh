if [ $# -eq 0 ]
	then
	version=$(git describe --long)
		jpackage -i ../jar -d ../deb --main-jar DecompressionPlanner-$version.jar -n 'Decompression Planner' --verbose --linux-shortcut --add-launcher 'Decompression Planner for high DPI screens'=highDPI.properties --app-version $version
	else
		jpackage -i ../jar -d ../deb --main-jar DecompressionPlanner-$1.jar -n 'Decompression Planner' --verbose --linux-shortcut --add-launcher 'Decompression Planner for high DPI screens'=highDPI.properties --app-version $1
fi
