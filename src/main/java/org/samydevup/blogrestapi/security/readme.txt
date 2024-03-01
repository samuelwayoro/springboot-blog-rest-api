****PR d'intégration de jwt dans un projet springboot maven *****

1-Ajouter les dépendances maven de jwt dans le projet : jjwt-impl / jjwt-api / jjwt-jackson

2-Créer une classe JwtAuthenticationEntryPoint (dans le package security): qui vas permettre de lever les exceptions lors des tentatives
  d'accès des users non autorisés . En fait cette classe vas devoir implémenter l'interface AuthenticationEntryPoint
  du package org.springframework.security.web; afin d'implémenter sa méthode commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException authException).
  Cette méthode sera appelée chaque fois qu'une exception due a une tentative d'accès d'un utilisateur non autorisé à une ressource.

3-Ajouter les propriétés Jwt dans les fichiers de propriétés (application.properties). Ces propriétés sont :
    - app.jwt-secret : une chaîne de caractère qui represente la clé secrète que le serveur utilise pour échanger avec le client
       Elle doit être doit être encodée en sha256 ou en autre algo avant d'être renseigné dans ce fichier.
    - app.jwt-expiration-milliseconds : qui es la durée de vie du token de l'utilisateur. Il est exprimé en millisecondes , alors
    il serait important de passer par un utilitaire (exemple : convertisseur google de jours en millisecondes)

4-Créer la class JwtTokenProvider annotée @Component dans le package security .
    Cette classe met à dispositions des méthodes utilitaires liées (méthode de traitements) du Jwt. Pour le faire elle contient :
    1-deux propriétés privées :
    - jwtSecret qui contient la valeur de la clé sécrète (encodée) et renseignée dans le fichier application.properties
    - jwtExpirationDate contenant aussi la durée de vie du Token en milliseconde renseignée aussi dans le même fichier.
    2-une première méthode generateToken() : comme son nom l'indique permet de génerer le token à partir de l'objet authentication
    3-une méthode getUsername() : permettant de retourner le username d'une requêtte client à partir de son token
    4-une méthode validateToken() : permettant de valider un token envoyé par un requête client . Attention cette méthode lance des
    exception en fonction de l'erreur liée au token


5-Creér le filtre JwtAuthenticationFilter(dans le package security) qui est responsable de l'authentification de l'utilisateur
 à partir de son token. Ce filtre contient deux propriétes : JwtTokenProvider et UserDetailsService utilisé dans la méthode doFilterIntenal()
 du filtre pour effectuer le traitement suivant sur le token :
     1- recup le token de la requêtte entrante après traitement à l'aide d'une méthode privée : getTokenFromRequest()
     2- valider le token : verifier si il non null ou vide ensuite valider le token avec la méthode validate() de la prop jwtTokenProvider
     3- recup le username dans le token à partir de la méthode getUsername de la prop jwtTokenProvider
     4- charger le user associé au token à partir de la méthode loadUserByUsernmane() de la classe UserDetailsService
     5- créer un objet de type : UsernamePasswordAuthenticationToken depuis spring security authentication
     6- ajouter l'objet request a l'objet authenticationToken
     7- stocker l'objet authenticationToken dans le context Spring
     8- passer au filtre les objets request et response : filterChain.doFilter(request,response);

6-Créer ensuite le payload (classe DTO) jwtAuthResponse qui servira a renvoyer le token au client suite a une connexion réussie.
    Il s'agit de créer une classe DTO dénommée JWTAuthresponse avec les props suivantes :  String accessToken;String tokenType="Bearer";
    l'utilisation de cette classe requière l'ajout d'une propriété de type JwtAuthenticationEntryPoint dans la classe SecurityConfig
    qui permettra de lever une exception lors d'une tentative d'accès d'un utilisateur non autorisé , partir de sa méthode
    securityFilterChain (NB : la méthode securityFilterChain() sert à mettre a disposition un Bean de configuration des accès aux
    endPoints de nos service Web .)

7-Effectuer la configuration de JWT dans Spring Security : configuration de la classe SecurityConfig
    Cette configuration sert a ajouter comme propriété un objet de type authenticationEntryPoint dans la classe SecurityConfig.
    Cette prop sera utilisée dans la méthode securityFilterChain() afin d'ajouter le filtre d'authentification : authenticationFilter avant
    le UsernamePasswordAuthenticationFilter.class à partir de l'objet http.addFilterBefore(). Ceci est très utile afin d'interdir
    l'accès aux endpoints au users non permis .


8- coder la méthode de connexion utilisateur (Login/Signin) dans le controller d'authentification afin de retourner maintenant un JWT Token
   en lieu et place d'une chaîne de caractère.
   Pour le faire :
   1-rajouter comme propriete la classe JwtTokenProvider dans la classe de service de connexion utilisateur (AuthServiceImpl dans ntre cas)
    -> afin d'utiliser sa methode generateToken() dans la méthode login() pour generer un token pour chaque nouvelle connexion user
   2-Configurer la méthode connexion utilisateur dans son controller de sorte a retourner un Objet de type  payload (classe DTO) jwtAuthResponse
     -> instancier l'objet DTO et le seter avec le token à partir de la méthode : setAccessToken(laValeurDuToken)