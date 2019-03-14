
项目Maven架构：
keymaoshop-parent：父工程，打包方式pom，管理jar包的版本号。
    |           项目中所有工程都应该继承父工程。
	|--keymaoshop-common：通用的工具类通用的pojo。打包方式jar
	|--keymaoshop-manager：服务层工程。聚合工程。Pom工程
		|--keymaoshop-manager-dao：打包方式jar
		|--keymaoshop-manager-pojo：打包方式jar
		|--keymaoshop-manager-interface：打包方式jar
		|--keymaoshop-manager-service：打包方式：jar
	|--keymaoshop-web：表现层工程。打包方式war
	|--keymaoshop-content：服务层工程。聚合工程。Pom工程
    		|--keymaoshop-content-interface：打包方式jar
    		|--keymaoshop-content-service：打包方式：jar
