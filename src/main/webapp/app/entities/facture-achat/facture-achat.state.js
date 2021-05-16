(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('facture-achat', {
            parent: 'entity',
            url: '/facture-achat',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.facture_achat.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/facture-achat/facture-achats.html',
                    controller: 'Facture_achatController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('facture_achat');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('facture-achat-detail', {
            parent: 'facture-achat',
            url: '/facture-achat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.facture_achat.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/facture-achat/facture-achat-detail.html',
                    controller: 'Facture_achatDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('facture_achat');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Facture_achat', function($stateParams, Facture_achat) {
                    return Facture_achat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'facture-achat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('facture-achat-detail.edit', {
            parent: 'facture-achat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/facture-achat/facture-achat-dialog.html',
                    controller: 'Facture_achatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Facture_achat', function(Facture_achat) {
                            return Facture_achat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('facture-achat.new', {
            parent: 'facture-achat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/facture-achat/facture-achat-dialog.html',
                    controller: 'Facture_achatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                referance: null,
                                date_facture: null,
                                echeance: null,
                                montant: null,
                                piece_jointe: null,
                                piece_jointeContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('facture-achat', null, { reload: 'facture-achat' });
                }, function() {
                    $state.go('facture-achat');
                });
            }]
        })
        .state('facture-achat.edit', {
            parent: 'facture-achat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/facture-achat/facture-achat-dialog.html',
                    controller: 'Facture_achatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Facture_achat', function(Facture_achat) {
                            return Facture_achat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('facture-achat', null, { reload: 'facture-achat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('facture-achat.delete', {
            parent: 'facture-achat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/facture-achat/facture-achat-delete-dialog.html',
                    controller: 'Facture_achatDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Facture_achat', function(Facture_achat) {
                            return Facture_achat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('facture-achat', null, { reload: 'facture-achat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
