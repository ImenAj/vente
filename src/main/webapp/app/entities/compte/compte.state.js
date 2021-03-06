(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('compte', {
            parent: 'entity',
            url: '/compte',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.compte.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/compte/comptes.html',
                    controller: 'CompteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('compte');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('compte-detail', {
            parent: 'compte',
            url: '/compte/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.compte.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/compte/compte-detail.html',
                    controller: 'CompteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('compte');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Compte', function($stateParams, Compte) {
                    return Compte.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'compte',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('compte-detail.edit', {
            parent: 'compte-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/compte/compte-dialog.html',
                    controller: 'CompteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Compte', function(Compte) {
                            return Compte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('compte.new', {
            parent: 'compte',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/compte/compte-dialog.html',
                    controller: 'CompteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                designation_compte: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('compte', null, { reload: 'compte' });
                }, function() {
                    $state.go('compte');
                });
            }]
        })
        .state('compte.edit', {
            parent: 'compte',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/compte/compte-dialog.html',
                    controller: 'CompteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Compte', function(Compte) {
                            return Compte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('compte', null, { reload: 'compte' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('compte.delete', {
            parent: 'compte',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/compte/compte-delete-dialog.html',
                    controller: 'CompteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Compte', function(Compte) {
                            return Compte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('compte', null, { reload: 'compte' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
