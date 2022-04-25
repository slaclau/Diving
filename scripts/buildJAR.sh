if [ $# -eq 0 ]; then
	version=$(git describe --long)
 slaclau
else
	version=$1
	git checkout $1
	version=$(git describe --long)
	
fi
jar cvfm ../jar/DecompressionPlanner-$version.jar Manifest.txt -C ../bin slaclau
