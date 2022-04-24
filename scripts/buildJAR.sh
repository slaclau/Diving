version=$(git describe)
jar cvfm ../jar/DecompressionPlanner-$version.jar Manifest.txt -C ../bin slaclau
