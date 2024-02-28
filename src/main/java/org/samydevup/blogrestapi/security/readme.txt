****process d'intégration de jwt dans un projet springboot maven *****

1-Ajouter les dépendances maven de jwt dans le projet : jjwt-impl / jjwt-api / jjwt-jackson

2-Créer une classe JwtAuthenticationEntryPoint (dans le package security): qui vas permettre de lever les exceptions lors des tentatives
  d'accès des users non autorisés . En fait cette classe vas devoir implémenter l'interface AuthenticationEntryPoint
  du package org.springframework.security.web; afin d'implémenter sa méthode commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException authException).
  Cette méthode sera appelée chaque fois qu'une exception due a une tentative d'accès d'un utilisateur non autorisé à une ressource.

3-Ajouter les propriétés Jwt dans les fichiers de propriétés (application.properties). Ces propriétés sont :
    - app.jwt-secret : une chaîne de caractère qui represente la clé secrète que le serveur utilise pour échanger avec le client
    Il doit être doit être encodée en sha256 ou en autre algo avant d'être renseigné dans le fichier
    - app.jwt-expiration-milliseconds : qui es la durée de vie du token de l'utilisateur. Il est exprimé en millisecondes , alors
    il serait important de passer par un utilitaire (exemple : convertisseur google de jours en millisecondes)

4-Créer la class JwtTokenProvider annotée @Component et aussi dans le package security .
    Cette classe met à dispositions des méthodes utilitaires liées (méthode de traitements) du Jwt. Pour le faire elle contient :
    1-deux propriétés privées :
    - jwtSecret qui contient la valeur de la clé sécrète (encodée) et renseignée dans le fichier application.properties
    - jwtExpirationDate contenant aussi la durée de vie du Token en milliseconde renseignée aussi dans le même fichier.
    2-une première méthode generateToken() : comme son nom l'indique permet de génerer le token à partir de l'objet authentication
    3-une méthode getUsername() : permettant de retourner le username d'une requêtte client à partir de son token
    4-une méthode validateToken() : permettant de valider un token envoyé par un requête client . Attention cette méthode lance des
    exception en fonction de l'erreur liée au token


5-Creér le filtre JwtAuthenticationFilter qui est responsable de l'authentification de l'utilisateur à partir de son token

6-Créer ensuite le payload (classe DTO) jwtAuthResponse qui servira a renvoyer le token au client suite a une connexion réussie

7-Effectuer la configuration de JWT dans Spring Security

8- coder la méthode de connexion utilisateur (Login/Signin) dans le controller d'authentification afin de retourner maintenant un JWT Token
   en lieu et place d'une chaîne de caractère.