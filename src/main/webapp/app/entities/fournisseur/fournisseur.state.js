(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fournisseur', {
            parent: 'entity',
            url: '/fournisseur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.fournisseur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fournisseur/fournisseurs.html',
                    controller: 'FournisseurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fournisseur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fournisseur-detail', {
            parent: 'fournisseur',
            url: '/fournisseur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.fournisseur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fournisseur/fournisseur-detail.html',
                    controller: 'FournisseurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fournisseur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fournisseur', function($stateParams, Fournisseur) {
                    return Fournisseur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'fournisseur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('fournisseur-detail.edit', {
            parent: 'fournisseur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fournisseur/fournisseur-dialog.html',
                    controller: 'FournisseurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fournisseur', function(Fournisseur) {
                            return Fournisseur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fournisseur.new', {
            parent: 'fournisseur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fournisseur/fournisseur-dialog.html',
                    controller: 'FournisseurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                matricule_fiscal: null,
                                raison_social: null,
                                adress: null,
                                ville: null,
                                code_postal: null,
                                tel: null,
                                fax: null,
                                site_web: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fournisseur', null, { reload: 'fournisseur' });
                }, function() {
                    $state.go('fournisseur');
                });
            }]
        })
        .state('fournisseur.edit', {
            parent: 'fournisseur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fournisseur/fournisseur-dialog.html',
                    controller: 'FournisseurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fournisseur', function(Fournisseur) {
                            return Fournisseur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fournisseur', null, { reload: 'fournisseur' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fournisseur.delete', {
            parent: 'fournisseur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fournisseur/fournisseur-delete-dialog.html',
                    controller: 'FournisseurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fournisseur', function(Fournisseur) {
                            return Fournisseur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fournisseur', null, { reload: 'fournisseur' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
